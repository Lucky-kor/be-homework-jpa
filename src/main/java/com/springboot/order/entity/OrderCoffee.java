package com.springboot.order.entity;

import com.springboot.coffee.entity.Coffee;
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
    private int orderCoffeeId;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name ="ORDER_ID")
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
//        주문이 들어오면 주문을 리스트에 포함한다.
        if(!order.getOrderCoffees().contains(this)) {
            order.setOrderCoffee(this);
        }
    }

    @ManyToOne
    @JoinColumn(name="COFFEE_ID")
    private Coffee coffee;

}
