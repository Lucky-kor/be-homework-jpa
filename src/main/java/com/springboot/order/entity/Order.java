package com.springboot.order.entity;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        if (member.getOrders().contains(this)) {
            member.setOrder(this);
        }
        this.member = member;
    }

//    영속성전이 -> 안에있는 리스트들의 변경값은 영속성컨텍스트에 없다. 껍데기만 변경되어 있기때문에
//    OneToMany mappedBy 필수 name = 나를 기본키로 가지고있는 객체의 변수명 현재 orderCoffee 안에 order
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderCoffee> orderCoffeeList = new ArrayList<>();
    public void setOrderCoffee(OrderCoffee orderCoffee){
        orderCoffeeList.add(orderCoffee);
//        메서드 실행 시점에 orderCoffee는 받은 시점에 자기자신을 주문으로 넣겠다.
        if(orderCoffee.getOrder()!= this){
            orderCoffee.setOrder(this);
        }
    }

    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 처리 완료"),
        ORDER_CANCEL(4, "주문 취소");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }
}