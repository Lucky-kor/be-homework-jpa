package com.springboot.order.entity;

import com.springboot.coffee.entity.Coffee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//Coffee와 Order의 관계는 N:M이므로 조인테이블인 OrderCoffee를 생성하고 객체끼리의 관계설정을 위해 @entity , @table 이름 명시
// 가져야할 필드값에 고유키, 생성전략은 자동
// 다대다에선 ManyToOne 으로 일단 연결하고 생각.

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDER_COFFEE")
public class OrderCoffee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderCoffeeId;
// 주문은 커피의 정보도 알고 있어야하므로 양방향연결을 해야 커피까지 조회가 가능함.
    @ManyToOne
    @JoinColumn(name = "ORDERS_ID")
    private Order order;
//    양방향 연결 해줄 떄는 서로를 호출 할 때 관계를 이어주는 메서드가 필요함 이름은 기본적으로 setXXX으로 정의한다
// Order entity에 orderCoffee 의 정보가 담긴 리스트가 있어야함
    public void setOrder(Order order){
        if(!order.getOrderCoffeeList().contains(this)){
            order.setOrderCoffee(this);
        }
        this.order = order;
    }


    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;


//  수량을 함께 받겠다.
    @Column(nullable = false)
    private int quantity = 0;

}
