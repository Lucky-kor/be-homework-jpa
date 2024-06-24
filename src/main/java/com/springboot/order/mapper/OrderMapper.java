package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;

import com.springboot.member.entity.Member;
import com.springboot.order.dto.OrderCoffeeResponseDto;
import com.springboot.order.dto.OrderPatchDto;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.dto.OrderResponseDto;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
            Order order = new Order();
            Member member = orderPostDto.getMember();
            order.setMember(member);
            List<OrderCoffee>orderCoffees=orderPostDto.getOrderCoffees()
                            .stream()
                            .map(orderCoffeeDto -> {
                                OrderCoffee orderCoffee = new OrderCoffee();
                                orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                                Coffee coffee = new Coffee();
                                coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                                orderCoffee.setCoffee(coffee);
                                orderCoffee.setOrder(order);
                                return orderCoffee;
                            }).collect(Collectors.toList());

            order.setOrderCoffees(orderCoffees);
        return order;
    }

    default Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto){
        Order order = new Order();
        order.setOrderId(orderPatchDto.getOrderId());
        order.setOrderStatus(orderPatchDto.getOrderStatus());
        return order;
    }

    default OrderResponseDto orderToOrderResponseDto(Order order) {
        return new OrderResponseDto(
                order.getOrderId(),
                order.getMember().getMemberId(),
                order.getOrderStatus(),
                orderToOrderCoffeeResponseDtos(order),
                order.getCreatedAt(),
                order.getMember().getStamp());
    }

    default List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders) {
           return  orders.stream()
                    .map(order -> orderToOrderResponseDto(order))
                    .collect(Collectors.toList());
    }

    default List<OrderCoffeeResponseDto> orderToOrderCoffeeResponseDtos(Order order) {

        List<OrderCoffeeResponseDto> result = order.getOrderCoffees()
                .stream()
                .map(orderCoffee ->
        {
            return new OrderCoffeeResponseDto(
                    orderCoffee.getCoffee().getCoffeeId(),
                    orderCoffee.getCoffee().getKorName(),
                    orderCoffee.getCoffee().getEngName(),
                    orderCoffee.getCoffee().getPrice(),
                    orderCoffee.getQuantity());
        }).collect(Collectors.toList());
        return result;
    }
//
//    default MultiResponseDto orderToPageResponseDto(Page<Order>orderPage){
//        PageInfo pageInfo = new PageInfo(orderPage.getNumber(),orderPage.getSize(),orderPage.getTotalElements(),orderPage.getTotalPages());
//
//        return new MultiResponseDto(orderPage.getContent(),(Page)pageInfo);
//    }


}