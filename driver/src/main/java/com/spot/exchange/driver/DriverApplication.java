package com.spot.exchange.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DriverApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DriverApplication.class, args);
		DriverService driverService = context.getBean(DriverService.class);
		driverService.executeOrderSequence();
	}

}
