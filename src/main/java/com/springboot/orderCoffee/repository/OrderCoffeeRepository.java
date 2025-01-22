package com.springboot.orderCoffee.repository;

import com.springboot.orderCoffee.entity.OrderCoffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCoffeeRepository extends JpaRepository<OrderCoffee, Long> {
}
