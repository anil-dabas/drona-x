package com.spot.exchange.me.model.book;

import com.spot.exchange.me.model.payload.OrderRequestPayload;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

public class OrderBook {
  private final NavigableMap<Integer, ArrayDeque<OrderRequestPayload>> buyOrders;
  private final NavigableMap<Integer, ArrayDeque<OrderRequestPayload>> sellOrders;

  public OrderBook() {
    this.buyOrders = new TreeMap<>(Comparator.reverseOrder()); // Highest price first
    this.sellOrders = new TreeMap<>(); // Lowest price first
  }

  public void addBuyOrder(OrderRequestPayload order) {
    buyOrders.computeIfAbsent(order.getPrice(), k -> new ArrayDeque<>(8)).add(order);
  }

  public void addSellOrder(OrderRequestPayload order) {
    sellOrders.computeIfAbsent(order.getPrice(), k -> new ArrayDeque<>(8)).add(order);
  }

  public ArrayDeque<OrderRequestPayload> getBestBuyOrders() {
    return buyOrders.isEmpty() ? new ArrayDeque<>() : buyOrders.firstEntry().getValue();
  }

  public ArrayDeque<OrderRequestPayload> getBestSellOrders() {
    return sellOrders.isEmpty() ? new ArrayDeque<>() : sellOrders.firstEntry().getValue();
  }

  public boolean removeOrder(OrderRequestPayload order) {
    return removeFromMap(buyOrders, order) || removeFromMap(sellOrders, order);
  }

  public void removePriceLevel(int price, boolean isBuy) {
    if (isBuy) {
      buyOrders.remove(price);
    } else {
      sellOrders.remove(price);
    }
  }

  private boolean removeFromMap(NavigableMap<Integer, ArrayDeque<OrderRequestPayload>> map,
      OrderRequestPayload order) {
    ArrayDeque<OrderRequestPayload> queue = map.get(order.getPrice());
    if (queue != null) {
      Iterator<OrderRequestPayload> iterator = queue.iterator();
      while (iterator.hasNext()) {
        if (iterator.next().getOrderId() == order.getOrderId()) {
          iterator.remove();
          if (queue.isEmpty()) {
            map.remove(order.getPrice());
          }
          return true;
        }
      }
    }
    return false;
  }
}