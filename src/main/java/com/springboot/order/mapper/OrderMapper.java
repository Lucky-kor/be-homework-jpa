package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.order.dto.*;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import lombok.Setter;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order resultOrder = new Order();
        resultOrder.setMember(orderPostDto.getMember());

        List<OrderCoffee> orderCoffeeList = orderPostDto.getOrderCoffees()
                .stream()
                .map(orderCoffeeDto -> {
                    Coffee coffee = new Coffee();
                    OrderCoffee orderCoffee = new OrderCoffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());

                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                    orderCoffee.setOrder(resultOrder);
                    orderCoffee.setCoffee(coffee);

                    return orderCoffee;
                })
                .collect(Collectors.toList());

        resultOrder.setOrderCoffeeList(orderCoffeeList);

        return resultOrder;
    }


    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);


    default OrderResponseDto orderToOrderResponseDto(Order order) {
        List<OrderCoffee> orderCoffees = order.getOrderCoffeeList();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setMemberId(order.getMember().getMemberId());
        orderResponseDto.setOrderStatus(order.getOrderStatus());
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        orderResponseDto.setOrderCoffees(
                orderCoffeesToOrderCoffeeResponseDtos(orderCoffees)
        );
        return orderResponseDto;
    };


    default List<OrderCoffeeResponseDto> orderCoffeesToOrderCoffeeResponseDtos(List<OrderCoffee> orderCoffees) {
        List<OrderCoffeeResponseDto> orderCoffeeResponseDtos = new ArrayList<>();

        for (OrderCoffee orderCoffee : orderCoffees) {
            OrderCoffeeResponseDto orderCoffeeResponseDto = new OrderCoffeeResponseDto(
                    orderCoffee.getCoffee().getCoffeeId(),
                    orderCoffee.getCoffee().getKorName(),
                    orderCoffee.getCoffee().getEngName(),
                    orderCoffee.getCoffee().getPrice(),
                    orderCoffee.getQuantity()
            );
            orderCoffeeResponseDtos.add(orderCoffeeResponseDto);
        }
        return orderCoffeeResponseDtos;
    }

}
