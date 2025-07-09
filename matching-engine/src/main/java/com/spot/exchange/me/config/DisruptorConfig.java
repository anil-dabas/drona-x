package com.spot.exchange.me.config;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.spot.exchange.me.engine.OrderHandler;
import com.spot.exchange.me.model.event.OrderEvent;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DisruptorConfig {

    @Bean
    public Disruptor<OrderEvent> disruptor(OrderHandler orderHandler) {
        EventFactory<OrderEvent> factory = OrderEvent::new;
        int bufferSize = 32768;//8388608;  //2097152;// 131072;//65536; // Must be a power of 2 // 131072, 2097152

        Disruptor<OrderEvent> disruptor = new Disruptor<>(
            factory, bufferSize, Executors.defaultThreadFactory(),
            ProducerType.MULTI, new BusySpinWaitStrategy()
        );
        disruptor.handleEventsWith(orderHandler);
        disruptor.start();
        return disruptor;
    }

    @Bean
    public RingBuffer<OrderEvent> ringBuffer(Disruptor<OrderEvent> disruptor) {
        return disruptor.getRingBuffer();
    }

}
