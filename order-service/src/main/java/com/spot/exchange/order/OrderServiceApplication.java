package com.spot.exchange.order;

import com.spot.exchange.order.cache.ListedPairsCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(OrderServiceApplication.class, args);
		ListedPairsCache pairsCache = context.getBean(ListedPairsCache.class);
		pairsCache.initCache();
	}

}
