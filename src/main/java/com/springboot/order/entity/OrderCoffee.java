package com.springboot.order.entity;

import com.springboot.coffee.entity.Coffee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderCoffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCoffeeId;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    public OrderCoffee(Coffee coffee, Order order, int quantity) {
        this.coffee = coffee;
        this.order = order;
        this.quantity = quantity;
    }

    public void addOrder(Order order){
        this.order = order;

        if(!order.getOrderCoffees().contains(this))
            order.addOrderCoffee(this);
    }

    @Column(nullable = false)
    private int quantity;
}
