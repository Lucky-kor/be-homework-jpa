package com.springboot.order.entity;

import com.springboot.coffee.entity.Coffee;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ORDER_COFFEE")
public class OrderCoffee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCoffeeId;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ORDERS_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;

    public OrderCoffee(int quantity, Coffee coffee, Order order) {
        this.quantity = quantity;
        this.coffee = coffee;
        this.order = order;
    }

    public void setOrder(Order order) {
        this.order = order;

        if (!order.getOrderCoffees().contains(this)) {
            order.getOrderCoffees().add(this);
        }
    }
}
