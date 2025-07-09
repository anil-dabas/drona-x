package com.spot.exchange.me.kafka.publisher;

import com.spot.exchange.proto.CanceledOrderPayload;
import com.spot.exchange.proto.CanceledOrderPayloadBatch;
import com.spot.exchange.proto.TradeResponse;
import com.spot.exchange.proto.TradeResponseBatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class TradePublisher {

  @Value("${kafka.topic.executed}")
  private String orderExecutedTopic;

  @Value("${kafka.topic.cancelled}")
  private String orderCancelledTopic;

  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  public TradePublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishExecutedTrade(TradeResponse tradeResponse) {
    try {
      byte[] message = tradeResponse.toByteArray();
      kafkaTemplate.send(orderExecutedTopic, message);
    } catch (Exception e) {
      log.error("Error while serializing Trade data: {}", e.getMessage());
    }
  }

  public void publishCancelledTrade(CanceledOrderPayload canceledOrderPayload) {
    try {
      byte[] message = canceledOrderPayload.toByteArray();
      kafkaTemplate.send(orderCancelledTopic, message);
    } catch (Exception e) {
      log.error("Error while serializing Cancelled Order data: {}", e.getMessage());
    }
  }

  public void publishExecutedTrades(Collection<TradeResponse> trades) {
    try {
      TradeResponseBatch.Builder batchBuilder = TradeResponseBatch.newBuilder();
      trades.forEach(batchBuilder::addTrades);
      byte[] message = batchBuilder.build().toByteArray();
      kafkaTemplate.send(orderExecutedTopic, message);
    } catch (Exception e) {
      log.error("Batch serialization failed, falling back to individual trades", e);
      trades.forEach(this::publishExecutedTrade);
    }
  }

  public void publishCancelledTrades(Collection<CanceledOrderPayload> cancellations) {
    try {
      CanceledOrderPayloadBatch.Builder batchBuilder = CanceledOrderPayloadBatch.newBuilder();
      cancellations.forEach(batchBuilder::addCancellations);
      byte[] message = batchBuilder.build().toByteArray();
      kafkaTemplate.send(orderCancelledTopic, message);
    } catch (Exception e) {
      log.error("Batch serialization failed, falling back to individual cancellations", e);
      cancellations.forEach(this::publishCancelledTrade);
    }
  }
}