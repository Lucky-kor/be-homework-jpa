package com.springboot.order.entity;

import com.springboot.coffee.entity.Coffee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Entity
@Setter
public class OrderCoffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCoffeeId;
    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;


    @ManyToOne
    @JoinColumn(name="ORDER_ID", nullable = false)
    private Order order;


    public void setOrder(Order order){
        this.order=order;
        if(!order.getOrderCoffees().contains(this)){
                order.getOrderCoffees().add(this);
                //order.setOrderCoffee(this) -> 이렇게도 가능.
        }
    }
}


