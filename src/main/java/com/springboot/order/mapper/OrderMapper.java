package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.order.dto.OrderCoffeeDto;
import com.springboot.order.dto.OrderPatchDto;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.dto.OrderResponseDto;
import com.springboot.order.entity.Order;
import com.springboot.ordercoffees.entity.OrderCoffee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order order = new Order();
        order.setMember(orderPostDto.getMember());

        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    OrderCoffee orderCoffee = new OrderCoffee();
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                    orderCoffee.setCoffee(coffee);
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                    return orderCoffee;
                })
                .collect(Collectors.toList());

        order.setOrderCoffees(orderCoffees);
        orderCoffees.forEach(orderCoffee -> orderCoffee.setOrder(order));
        return order;
    }

    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);
    OrderResponseDto orderToOrderResponseDto(Order order);
    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);
}
