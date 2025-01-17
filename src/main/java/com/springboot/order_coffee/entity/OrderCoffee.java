package com.springboot.order_coffee.entity;

import com.springboot.coffee.entity.Coffee;
import com.springboot.order.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderCoffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderCoffeeId;

    @ManyToOne
    @JoinColumn(name = "ORDERS_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;

    @Column(nullable = false)
    private int quantity;

    public OrderCoffee(int quantity, Coffee coffee, Order order) {
        this.quantity = quantity;
        this.coffee = coffee;
        this.order = order;
    }

    public void setOrder(Order order) {
        this.order = order;

        if (!order.getOrderCoffees().contains(this)) {
            order.setOrderCoffee(this);
        }
    }

}
