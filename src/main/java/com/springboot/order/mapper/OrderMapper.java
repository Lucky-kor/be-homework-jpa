package com.springboot.order.mapper;

import com.springboot.coffee.dto.CoffeeResponseDto;
import com.springboot.coffee.entity.Coffee;
import com.springboot.member.entity.Member;
import com.springboot.order.dto.*;
import com.springboot.order.entity.Order;
import com.springboot.orderCoffee.entity.OrderCoffee;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {
//    Order orderPostDtoToOrder(OrderPostDto orderPostDto);
//
//    default OrderCoffee orderCoffeeDtoToOrderCoffee(OrderCoffeeDto orderCoffeeDto) {
//        OrderCoffee orderCoffee = new OrderCoffee();
//        orderCoffee.setQuantity(orderCoffee.getQuantity());
//        orderCoffee.setOrderCoffeeId(orderCoffee.getOrderCoffeeId());
//
//        return orderCoffee;
//    }

    default OrderCoffee orderCoffeeDtoToOrderCoffee(OrderCoffeeDto orderCoffeeDto){
        OrderCoffee orderCoffee = new OrderCoffee();
        orderCoffee.setQuantity(orderCoffeeDto.getQuantity());
        Coffee coffee = new Coffee();
        coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());
        Order order = new Order();
        List<OrderCoffee>orderCoffees = new ArrayList<>();
        order.setOrderCoffees(orderCoffees);

        orderCoffee.setCoffee(coffee);

        orderCoffee.setOrder(order);
        order.setOrderCoffee(orderCoffee);

        return orderCoffee;
    }


    // 얘 자동 매핑 쓰면
    // orderPostDto.orderCoffees.coffeeId -> order.orderCoffees.coffee.coffeeId 로 자동 매핑 안됨
    // 직접 해줘야함
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order order = new Order();

        // 멤버 새로 만들어서
        Member member = new Member();
        // 멤버에 memberId 새로 세팅 해줘야함
        member.setMemberId(orderPostDto.getMemberId());
        // 세팅 끝난 member 를 order 에 넣어준다.
        order.setMember(member);

        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                // 여기 map 돌면서 coffee 객체 만들어서 직접 돌려야함
                .map(orderCoffeeDto -> orderCoffeeDtoToOrderCoffee(orderCoffeeDto))
                .collect(Collectors.toList());


        order.setOrderCoffees(orderCoffees);

        return order;
    }


//    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);
    default Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto) {
        Order order = new Order();
        order.setOrderId(orderPatchDto.getOrderId());
        order.setOrderStatus(orderPatchDto.getOrderStatus());

        return order;
    }

    OrderCoffeeDto orderCoffeeToOrderCoffeeDto (OrderCoffee orderCoffee);


     default OrderResponseDto orderToOrderResponseDto(Order order) {

         List<OrderCoffeeResponseDto> orderCoffeeResponseDtos = order.getOrderCoffees().stream()
                 .map(orderCoffee -> new OrderCoffeeResponseDto(
                         orderCoffee.getCoffee().getCoffeeId(),
                         orderCoffee.getCoffee().getKorName(),
                         orderCoffee.getCoffee().getEngName(),
                         orderCoffee.getCoffee().getPrice(),
                         orderCoffee.getQuantity()
                 )).collect(Collectors.toList());

         OrderResponseDto orderResponseDto = new OrderResponseDto();

         orderResponseDto.setOrderId(order.getOrderId());
         orderResponseDto.setMemberId(order.getMember().getMemberId());
         orderResponseDto.setOrderStatus(order.getOrderStatus());
         orderResponseDto.setOrderCoffees(orderCoffeeResponseDtos);
         orderResponseDto.setCreatedAt(order.getCreatedAt());
         orderResponseDto.setModifiedAt(order.getModifiedAt());
         
        return orderResponseDto;
     }

    default List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders){
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(order -> orderToOrderResponseDto(order))
                .collect(Collectors.toList());
        return orderResponseDtos;
    }


}
