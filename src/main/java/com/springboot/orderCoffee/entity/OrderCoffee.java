package com.springboot.orderCoffee.entity;

import com.springboot.coffee.entity.Coffee;
import com.springboot.order.entity.Order;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class OrderCoffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCoffeeId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
        if(!order.getOrderCoffees().contains(this)) {
            order.setOrderCoffee(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "coffee_id")
    private Coffee coffee;

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
        if(!coffee.getOrderCoffees().contains(this)) {
            coffee.getOrderCoffees().add(this);
        }
    }



    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();


}
