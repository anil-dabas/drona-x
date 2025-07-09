package com.spot.exchange.me.kafka.consumer;

import static com.spot.exchange.me.model.payload.OrderRequestPayload.convertToOrderRequestPayloadModel;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lmax.disruptor.RingBuffer;
import com.spot.exchange.me.model.event.OrderEvent;
import com.spot.exchange.me.model.payload.OrderRequestPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumer {

  private final RingBuffer<OrderEvent> ringBuffer;

  public OrderConsumer(RingBuffer<OrderEvent> ringBuffer) {
    this.ringBuffer = ringBuffer;
  }

  @KafkaListener(topics = "${kafka.topic.order}", groupId = "order_group")
  public void consume(byte[] message) {
    try {
      com.spot.exchange.proto.OrderRequestPayload protoPayload = com.spot.exchange.proto.OrderRequestPayload.parseFrom(message);
      OrderRequestPayload payload = convertToOrderRequestPayloadModel(protoPayload);
      long sequence = ringBuffer.next();
      OrderEvent event = ringBuffer.get(sequence);
      event.setOrder(payload);
      ringBuffer.publish(sequence);
    } catch (InvalidProtocolBufferException e) {
      log.error("Error processing message: {}", e.getMessage());
    }
  }
}