package com.spot.exchange.order.kafka;

import com.spot.exchange.proto.OrderRequestPayload;
import com.spot.exchange.proto.UpdateOrderPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaOrderPublisher {

    @Value("${kafka.topic.order}")
    private String orderTopic;

    @Value("${kafka.topic.orderUpdate}")
    private String orderUpdateTopic;

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaOrderPublisher(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrder(OrderRequestPayload order) {
        try {
            byte[] message = order.toByteArray();
            kafkaTemplate.send(orderTopic, message);
        } catch (Exception e) {
            log.error("Error while serializing Order data: {}", e.getMessage());
        }
    }

    public void publishOrderUpdate(UpdateOrderPayload updateOrderPayload) {
        try {
            byte[] message = updateOrderPayload.toByteArray();
            kafkaTemplate.send(orderUpdateTopic, message);
            log.info("Publishing OrderUpdate to Kafka");
        } catch (Exception e) {
            log.info("Error while serializing OrderUpdate data: {}", e.getMessage());
        }
    }
}