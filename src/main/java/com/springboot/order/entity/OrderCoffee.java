package com.springboot.order.entity;

import com.springboot.coffee.entity.Coffee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "ORDER_COFFEE")
@Entity
public class OrderCoffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderCoffeeId;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ORDERS_ID")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
        if (!order.getOrderCoffeeList().contains(this)) {
            order.getOrderCoffeeList().add(this);
        }
    }

}