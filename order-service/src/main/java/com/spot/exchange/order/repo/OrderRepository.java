package com.spot.exchange.order.repo;

import com.spot.exchange.order.model.domain.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Order findByOrderId(long orderId);
}
