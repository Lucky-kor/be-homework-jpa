package com.springboot.order.dto;


import com.springboot.member.entity.Member;
import com.springboot.member.entity.Stamp;
import com.springboot.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {
    private long orderId;
    private long memberId;
    private Order.OrderStatus orderStatus;
    private List<OrderCoffeeResponseDto> orderCoffees;
    private LocalDateTime createdAt;
    private Stamp stamp;

    public void setMember(Member member) {
        this.memberId = member.getMemberId();
    }
}
