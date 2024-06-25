package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.member.entity.Member;
import com.springboot.order.dto.OrderCoffeeResponseDto;
import com.springboot.order.dto.OrderPatchDto;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.dto.OrderResponseDto;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto){
        Order order = new Order();
        Member member = new Member();
        member.setMemberId(orderPostDto.getMemberId());

        List<OrderCoffee> orderCoffeeList = orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    OrderCoffee orderCoffee = new OrderCoffee();
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                    orderCoffee.setOrder(order);
                    orderCoffee.setCoffee(coffee);
                    return orderCoffee;
                }).collect(Collectors.toList());
        order.setMember(member);
        return order;
    }
    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);


    default OrderResponseDto orderToOrderResponseDto(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setMemberId(order.getMember().getMemberId());
        orderResponseDto.setOrderStatus(order.getOrderStatus());
        List<OrderCoffeeResponseDto> orderCoffeeResponseDtos = order.getOrderCoffeeList().stream()
                .map(orderCoffee -> orderCoffeeToOrderCoffeeResponseDto(orderCoffee))
                .collect(Collectors.toList());
        orderResponseDto.setOrderCoffees(orderCoffeeResponseDtos);
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        return orderResponseDto;
    }
//    @Mapping(source = "member.memberId", target = "memberId");
//    OrderResponseDto orderToOrderResponseDto(Order order);


//     @Mapping(source = "coffee.coffeeId", target = "coffeeId")
//     @Mapping(source = "coffee.korName", target = "korName")
//     @Mapping(source = "coffee.engName", target = "engName")
//     @Mapping(source = "coffee.price", target = "price")
//    OrderCoffeeResponseDto orderCoffeeToORderCoffeeResponseDto(OrderCoffee orderCoffee);


    default OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee){
        OrderCoffeeResponseDto orderCoffeeResponseDto = new OrderCoffeeResponseDto(
                orderCoffee.getCoffee().getCoffeeId(),
                orderCoffee.getCoffee().getKorName(),
                orderCoffee.getCoffee().getEngName(),
                orderCoffee.getCoffee().getPrice(),
                orderCoffee.getQuantity()
        );
        return orderCoffeeResponseDto;
    }


    default List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders){
        return orders.stream()
                .map(this::orderToOrderResponseDto).collect(Collectors.toList());
    }
}
