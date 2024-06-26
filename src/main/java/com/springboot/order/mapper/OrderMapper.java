package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.order.dto.*;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto){
        Order order = new Order();

        order.setMember(orderPostDto.getMember());

        orderPostDto.getOrderCoffees().stream()
                .map(x -> {
            OrderCoffee orderCoffee = new OrderCoffee();
            orderCoffee.setCoffee(new Coffee(x.getCoffeeId()));
            order.addOrderCoffee(orderCoffee);
                    orderCoffee.setQuantity(x.getQuantity());
            return orderCoffee;
        }).collect(Collectors.toList());

        return order;
    };
    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);

//    @Mapping(source = "member.memberId", target = "memberId")
//    OrderResponseDto orderToOrderResponseDto(Order order);

    default OrderResponseDto orderToOrderResponseDto(Order order){
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrderStatus(order.getOrderStatus());
        responseDto.setOrderId(order.getOrderId());
        responseDto.setMember(order.getMember());
        responseDto.setCreatedAt(order.getCreatedAt());
        responseDto.setOrderCoffees(orderCoffeeToOrderCoffeeResponseDto(order));
        return responseDto;
    };

    default List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders){
        return orders.stream()
                .map(x -> orderToOrderResponseDto(x))
                .collect(Collectors.toList());
    };

    default OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee) {
       return new OrderCoffeeResponseDto(
               orderCoffee.getCoffee().getCoffeeId()
               , orderCoffee.getCoffee().getKorName()
               , orderCoffee.getCoffee().getEngName()
               , orderCoffee.getCoffee().getPrice(),
               orderCoffee.getQuantity()
       );
    }

//    @Mapping(source = "coffee.coffeeId", target = "coffeeId")
//    @Mapping(source = "coffee.korName", target = "korName")
//    @Mapping(source = "coffee.engName", target = "engName")
//    @Mapping(source = "coffee.price", target = "price")
//    OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee);

    default List<OrderCoffeeResponseDto> orderCoffeeToOrderCoffeeResponseDto(Order order){
        List<OrderCoffeeResponseDto> orderCoffeeResponseDtos = new ArrayList<>();

        System.out.println("order.getOrderCoffees() : " + order.getOrderCoffees().size());

        orderCoffeeResponseDtos = order.getOrderCoffees().stream()
                .map(orderCoffee -> orderCoffeeToOrderCoffeeResponseDto(orderCoffee))
                .collect(Collectors.toList());

        return orderCoffeeResponseDtos;
    }
}
