package com.spot.exchange.order.kafka;

import static com.spot.exchange.order.model.payload.listener.CanceledOrderPayload.convertToCanceledOrderPayloadModel;
import static com.spot.exchange.order.model.response.TradeResponse.convertToTradeResponseModel;

import com.google.protobuf.InvalidProtocolBufferException;
import com.spot.exchange.order.service.OrderService;
import com.spot.exchange.proto.CanceledOrderPayload;
import com.spot.exchange.proto.CanceledOrderPayloadBatch;
import com.spot.exchange.proto.TradeResponse;
import com.spot.exchange.proto.TradeResponseBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class KafkaOrderListener {

  private final OrderService orderService;

  @Autowired
  public KafkaOrderListener(OrderService orderService) {
    this.orderService = orderService;
  }

  @KafkaListener(topics = "${kafka.topic.orderstatus.matched}", groupId = "order_group")
  public void listenMatchedOrdersUpdate(byte[] message) {
    try {
      try {
        // Try to parse as a batch
        TradeResponseBatch batch = TradeResponseBatch.parseFrom(message);
        List<TradeResponse> trades = batch.getTradesList();
        trades.forEach(trade -> {
          orderService.updateMatchedOrderDetailsAndPublishResponse(
              convertToTradeResponseModel(trade));
          log.debug("Processed trade from batch: {}", trade);
        });
        log.info("Processed batch of {} matched trades", trades.size());
      } catch (InvalidProtocolBufferException e) {
        // Fallback to single TradeResponse
        TradeResponse trade = TradeResponse.parseFrom(message);
        orderService.updateMatchedOrderDetailsAndPublishResponse(
            convertToTradeResponseModel(trade));
        log.info("Processed single matched trade: {}", trade);
      }
    } catch (Exception e) {
      log.error("Error processing matched order message: {}", e.getMessage());
    }
  }

  @KafkaListener(topics = "${kafka.topic.orderstatus.canceled}", groupId = "order_group")
  public void listenCanceledOrdersUpdate(byte[] message) {
    try {
      try {
        // Try to parse as a batch
        CanceledOrderPayloadBatch batch = CanceledOrderPayloadBatch.parseFrom(message);
        List<CanceledOrderPayload> cancellations = batch.getCancellationsList();
        cancellations.forEach(cancel -> {
          orderService.updateCanceledOrderDetailsAndPublishResponse(
              convertToCanceledOrderPayloadModel(cancel));
          log.debug("Processed cancellation from batch: {}", cancel);
        });
        log.info("Processed batch of {} cancellations", cancellations.size());
      } catch (InvalidProtocolBufferException e) {
        // Fallback to single CanceledOrderPayload
        CanceledOrderPayload cancel = CanceledOrderPayload.parseFrom(message);
        orderService.updateCanceledOrderDetailsAndPublishResponse(
            convertToCanceledOrderPayloadModel(cancel));
        log.info("Processed single cancellation: {}", cancel);
      }
    } catch (Exception e) {
      log.error("Error processing cancelled order message: {}", e.getMessage());
    }
  }


}