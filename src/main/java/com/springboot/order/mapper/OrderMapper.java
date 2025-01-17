package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.order.dto.OrderCoffeeDto;
import com.springboot.order.dto.OrderPatchDto;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.dto.OrderResponseDto;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order orderPostDtoToOrder(OrderPostDto orderPostDto);

    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);

    OrderResponseDto orderToOrderResponseDto(Order order);

    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

////
//    default Order orderPostDtoToOrder(OrderPostDto dto) {
//        Order order = new Order();
//        Member member = new Member();
//        member.setMemberId(order.getMember().getMemberId());
//        order.setMember(member);
//
//        List<OrderCoffee> orderCoffees = dto.getOrderCoffees().stream()
//                .map(orderCoffeeDto -> {
//                    Coffee coffee = new Coffee();
//                    OrderCoffee orderCoffee = new OrderCoffee();
//                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
//
//
//                } )
//
//    }

}
