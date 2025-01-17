package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.order.dto.*;
import com.springboot.order.entity.Order;
import com.springboot.order_coffee.entity.OrderCoffee;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDto orderToOrderResponseDto(Order order);
    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);
    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

    default OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee) {
        OrderCoffeeResponseDto orderCoffeeResponseDto = new OrderCoffeeResponseDto();

        orderCoffeeResponseDto.setQuantity(orderCoffee.getQuantity());
        orderCoffeeResponseDto.setPrice(orderCoffee.getCoffee().getPrice());
        orderCoffeeResponseDto.setKorName(orderCoffee.getCoffee().getKorName());
        orderCoffeeResponseDto.setEngName(orderCoffee.getCoffee().getEngName());
        orderCoffeeResponseDto.setCoffeeId(orderCoffee.getOrderCoffeeId());

        return orderCoffeeResponseDto;
    }

    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order order = new Order();

        order.setMember(orderPostDto.getMember());

        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    OrderCoffee orderCoffee = new OrderCoffee();

                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());

                    orderCoffee.setCoffee(coffee);

                    orderCoffee.setOrder(order);

                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());

                    return orderCoffee;
                }).collect(Collectors.toList());

        order.setOrderCoffees(orderCoffees);

        return order;
    }
}
