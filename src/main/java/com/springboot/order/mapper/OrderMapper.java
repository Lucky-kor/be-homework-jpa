package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.member.entity.Member;
import com.springboot.order.dto.OrderCoffeeResponseDto;
import com.springboot.order.dto.OrderPatchDto;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.dto.OrderResponseDto;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import com.springboot.stamp.entity.Stamp;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto){
        Order order = new Order();
        Member member = new Member();

        member.setMemberId(orderPostDto.getMemberId());

        orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    OrderCoffee orderCoffee = new OrderCoffee();
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
                    orderCoffee.setOrder(order);
                    orderCoffee.setCoffee(coffee);
                    //orderCoffee.setOrderCoffeeId(orderCoffeeDto.getCoffeeId());
                    return orderCoffee;
                }).collect(Collectors.toList());
        order.setMember(member);
        return order;
    }
    /*default OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee){
        OrderCoffeeResponseDto orderCoffeeResponseDto = new OrderCoffeeResponseDto();
        orderCoffeeResponseDto.setCoffeeId(orderCoffee.getCoffee().getCoffeeId());
        orderCoffeeResponseDto.setKorName(orderCoffee.getCoffee().getKorName());
        orderCoffeeResponseDto.setEngName(orderCoffee.getCoffee().getEngName());
        orderCoffeeResponseDto.setPrice(orderCoffee.getCoffee().getPrice());
        orderCoffeeResponseDto.setQuantity(orderCoffee.getQuantity());
        return orderCoffeeResponseDto;
    }*/
    @Mapping(source = "coffee.coffeeId", target = "coffeeId")
    @Mapping(source = "coffee.korName", target = "korName")
    @Mapping(source = "coffee.engName", target = "engName")
    @Mapping(source = "coffee.price", target = "price")
    OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee);
    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);
    default OrderResponseDto orderToOrderResponseDto(Order order){
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setMemberId(order.getMember().getMemberId());
        orderResponseDto.setOrderStatus(order.getOrderStatus());
        List<OrderCoffeeResponseDto> orderCoffeeResponseDtoList = order.getOrderCoffeeList().stream()
                        .map(orderCoffee -> orderCoffeeToOrderCoffeeResponseDto(orderCoffee))
                        .collect(Collectors.toList());
        orderResponseDto.setOrderCoffees(orderCoffeeResponseDtoList);
        orderResponseDto.setCreatedAt(order.getCreatedAt());
        return orderResponseDto;
    }
    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);
}
