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
        // 새로운 Order 객체를 생성
        Order order = new Order();

        // 새로운 Member 객체를 생성하고, orderPostDto 에서 memberId를 설정
        Member member = new Member();
        member.setMemberId(orderPostDto.getMemberId());

        // 생성한 회원 객체를 주문 객체에 설정
        order.setMember(member);

        // 주문 Dto 에 포함된 커피 주문 목록을 가져와서 새로운 OrderCoffee 목록을 만들기
        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees().stream()
                .map(orderCoffeeDto -> {
                    // 새로운 OrderCoffee 객체를 생성하고, 수량을 설정합니다.
                    OrderCoffee orderCoffee = new OrderCoffee();
                    orderCoffee.setQuantity(orderCoffeeDto.getQuantity());

                    // 새로운 Coffee 객체를 생성하고, 커피 ID를 설정합니다.
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeId(orderCoffeeDto.getCoffeeId());

                    // 생성한 Coffee 객체를 OrderCoffee 객체에 설정합니다.
                    orderCoffee.setCoffee(coffee);

                    // OrderCoffee 객체에 주문(Order) 객체를 설정합니다.
                    orderCoffee.setOrder(order);

                    // OrderCoffee 객체를 반환합니다.
                    return orderCoffee;
                }).collect(Collectors.toList());

        // 생성한 OrderCoffee 목록을 주문(Order) 객체에 설정합니다.
        order.setOrderCoffees(orderCoffees);

        // 최종적으로 완성된 주문(Order) 객체를 반환합니다.
        return order;
    }


    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);

    @Mapping(source = "member.memberId", target = "memberId")
    OrderResponseDto orderToOrderResponseDto(Order order);

    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

//    default OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee) {
//        OrderCoffeeResponseDto orderCoffeeResponseDto = new OrderCoffeeResponseDto();
//        orderCoffeeResponseDto.setQuantity(orderCoffeeResponseDto.getQuantity());
//        Coffee coffee = orderCoffee.getCoffee();
//        orderCoffeeResponseDto.setCoffeeId(coffee.getCoffeeId());
//        orderCoffeeResponseDto.setKorName(coffee.getKorName());
//        orderCoffeeResponseDto.setEngName(coffee.getEngName());
//        orderCoffeeResponseDto.setPrice(coffee.getPrice());
//        return orderCoffeeResponseDto;
//    }

    @Mapping(source = "coffee.coffeeId", target = "coffeeId")
    @Mapping(source = "coffee.korName", target = "korName")
    @Mapping(source = "coffee.engName", target = "engName")
    @Mapping(source = "coffee.price", target = "price")
    OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee);
}
