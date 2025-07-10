package com.spot.exchange.me.engine;

import com.lmax.disruptor.EventHandler;
import com.spot.exchange.me.kafka.publisher.TradePublisher;
import com.spot.exchange.me.model.book.OrderBook;
import com.spot.exchange.me.model.event.OrderEvent;
import com.spot.exchange.me.model.payload.OrderRequestPayload;
import com.spot.exchange.proto.CanceledOrderPayload;
import com.spot.exchange.proto.TradeResponse;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderHandler implements EventHandler<OrderEvent> {
  private static final int BATCH_SIZE = 100;
  private static final int CANCEL_BATCH_SIZE = 1000;
  private static final String SYMBOL = "BTC-USDT";

  private final OrderBook orderBook = new OrderBook();
  private final ArrayDeque<TradeResponse> tradeBatch = new ArrayDeque<>(BATCH_SIZE);
  private final ArrayDeque<CanceledOrderPayload> cancelBatch = new ArrayDeque<>(CANCEL_BATCH_SIZE);

  // Benchmarking fields
  private long firstOrderTimestamp = -1;
  private long lastOrderTimestamp = -1;
  private final long expectedOrderCount = 500_000;
  private final AtomicLong orderCount = new AtomicLong(0);

  @Autowired
  private TradePublisher tradePublisher;

  @Override
  public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) {
    final OrderRequestPayload order = event.getOrder();
    long currentTime = System.nanoTime();

    try {
      // Record first order timestamp
      if (firstOrderTimestamp == -1) {
        firstOrderTimestamp = currentTime;
      }

      switch (order.getOrderAction()) {
        case 1 -> handleCancel(order);
        case 2 -> handleNewOrder(order, endOfBatch);
        default -> {} // Invalid action
      }
    } catch (Exception e) {
      log.error("Error processing order: {}", e.getMessage(), e);
    } finally {
      event.clear(); // Ensure the event is cleared
      lastOrderTimestamp = currentTime;
      long count = orderCount.incrementAndGet();

      if (count == expectedOrderCount) {
        double totalTimeMs = (lastOrderTimestamp - firstOrderTimestamp) / 1_000_000.0;
        log.info("Processed {} orders in {} ms ({} ops/sec)",
            orderCount.get(), totalTimeMs, (orderCount.get() * 1_000_000_000L) / (lastOrderTimestamp - firstOrderTimestamp));
      }
    }
  }

  private void handleNewOrder(OrderRequestPayload order, boolean endOfBatch) {
    if (order.getSide() == 1) {
      orderBook.addBuyOrder(order);
    } else {
      orderBook.addSellOrder(order);
    }
    matchOrders(endOfBatch);
  }

  private void handleCancel(OrderRequestPayload order) {
    if (orderBook.removeOrder(order)) {
      publishCancellation(order.getOrderId());
    }
  }

  private void matchOrders(boolean endOfBatch) {
    while (true) {
      final var topBuys = orderBook.getBestBuyOrders();
      final var topSells = orderBook.getBestSellOrders();

      if (topBuys.isEmpty() || topSells.isEmpty()) {
        break;
      }

      final var bestBuy = topBuys.getFirst();
      final var bestSell = topSells.getFirst();

      if (bestBuy.getPrice() < bestSell.getPrice()) {
        break;
      }

      final int matchedQty = Math.min(bestBuy.getQuantity(), bestSell.getQuantity());
      createTrade(bestBuy, bestSell, matchedQty);

      updateOrderQuantities(bestBuy, bestSell, matchedQty);
      cleanupFilledOrders(topBuys, topSells, bestBuy, bestSell);
    }

    if (endOfBatch) {
      if (!tradeBatch.isEmpty()) {
        tradePublisher.publishExecutedTrades(tradeBatch);
        tradeBatch.clear();
      }
      if (!cancelBatch.isEmpty()) {
        tradePublisher.publishCancelledTrades(cancelBatch);
        cancelBatch.clear();
      }
    }
  }

  private void createTrade(OrderRequestPayload buy, OrderRequestPayload sell, int qty) {
    boolean buyerIsMaker = buy.getTimestamp() < sell.getTimestamp();
    int price = buyerIsMaker ? buy.getPrice() : sell.getPrice();

    tradeBatch.add(TradeResponse.newBuilder()
        .setId(System.nanoTime())
        .setSymbol(SYMBOL)
        .setBuy(buy.getOrderId())
        .setSell(sell.getOrderId())
        .setQuantity(qty)
        .setPrice(price)
        .setIsBuyerMaker(buyerIsMaker)
        .setTimestamp(System.nanoTime())
        .build());

    if (tradeBatch.size() >= BATCH_SIZE) {
      tradePublisher.publishExecutedTrades(tradeBatch);
      tradeBatch.clear();
    }
  }

  private void updateOrderQuantities(OrderRequestPayload buy, OrderRequestPayload sell, int matchedQty) {
    buy.setQuantity(buy.getQuantity() - matchedQty);
    sell.setQuantity(sell.getQuantity() - matchedQty);
  }

  private void cleanupFilledOrders(ArrayDeque<OrderRequestPayload> buys,
      ArrayDeque<OrderRequestPayload> sells,
      OrderRequestPayload buy,
      OrderRequestPayload sell) {
    if (buy.getQuantity() == 0) {
      buys.removeFirst();
      if (buys.isEmpty()) {
        orderBook.removePriceLevel(buy.getPrice(), true);
      }
    }
    if (sell.getQuantity() == 0) {
      sells.removeFirst();
      if (sells.isEmpty()) {
        orderBook.removePriceLevel(sell.getPrice(), false);
      }
    }
  }

  private void publishCancellation(long orderId) {
    cancelBatch.add(CanceledOrderPayload.newBuilder()
        .setOrderId(orderId)
        .setSymbol(SYMBOL)
        .setTimestamp(System.nanoTime())
        .build());

    if (cancelBatch.size() >= CANCEL_BATCH_SIZE) {
      tradePublisher.publishCancelledTrades(cancelBatch);
      cancelBatch.clear();
    }
  }
}