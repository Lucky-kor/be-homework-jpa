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
import org.mapstruct.Mapping;


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

    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);
   /*     Order order = new Order();
        order.setOrderId(orderPatchDto.getOrderId());
        order.setOrderStatus(orderPatchDto.getOrderStatus());
        return order;*/



    @Mapping(source= "member.memberId", target = "memberId")
    @Mapping(source = "member.stamp", target = "stamp")
   OrderResponseDto orderToOrderResponseDto(Order order);
    /* {
        return new OrderResponseDto(
                order.getOrderId(),
                order.getMember().getMemberId(),
                order.getOrderStatus(),
                orderToOrderCoffeeResponseDtos(order),
                order.getCreatedAt(),
                order.getMember().getStamp());
    }*/

   List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

/*    default List<OrderCoffeeResponseDto> orderToOrderCoffeeResponseDtos(Order order) {

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
    }*/


    @Mapping(source = "coffee.coffeeId", target ="coffeeId")
    @Mapping(source = "coffee.korName", target ="korName")
    @Mapping(source="coffee.engName",target="engName")
    @Mapping(source="coffee.price",target="price")
    OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee);


}