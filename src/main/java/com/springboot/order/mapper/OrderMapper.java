package com.springboot.order.mapper;

import com.springboot.coffee.entity.Coffee;
import com.springboot.order.dto.*;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;



@Mapper(componentModel = "spring")
public interface OrderMapper {
    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) {
        Order order = new Order ();
        OrderCoffee orderCoffee = new OrderCoffee ();

        order.setMember ( orderPostDto.getMember () );

        List<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees ().stream ()
                .map ( orderCoffeeDto ->
                        {
                            Coffee coffee = new Coffee ();
                            coffee.setCoffeeId ( orderCoffeeDto.getCoffeeId () );
                            orderCoffee.setCoffee ( coffee );
                            orderCoffee.setOrder ( order );
                            orderCoffee.setQuantity ( orderCoffeeDto.getQuantity () );
                            return orderCoffee;
                        }
                )
                .collect ( Collectors.toList () );
        order.setOrderCoffees ( orderCoffees );
        return order;


//        order.getMember().setMemberId(orderPostDto.getMemberId());
//        List<OrderCoffee> orderCoffees = (orderPostDto.getOrderCoffees().stream()
//                .map(orderCoffeeDto -> orderCoffeeDtoToOrderCoffee(orderCoffeeDto))
//                .collect( Collectors.toList()));
//        order.setOrderCoffees(orderCoffees);
//        Coffee coffee = new Coffee();
//        List<Long> coffeeId = orderPostDto.getOrderCoffees().stream()
//                .map(orderCoffeeDto -> orderCoffeeDto.getCoffeeId())
//                .collect( Collectors.toList());
//        coffee.setCoffeeId()
    }

    ;

    Order orderPatchDtoToOrder(OrderPatchDto orderPatchDto);

//    default OrderResponseDto orderToOrderResponseDto(Order order) {
//        OrderResponseDto orderResponseDto = new OrderResponseDto();
//        orderResponseDto.setOrderId(order.getOrderId());
//        orderResponseDto.setMemberId(order.getMember().getMemberId());
//        orderResponseDto.setOrderStatus(order.getOrderStatus());
//        List<OrderCoffeeResponseDto>orderCoffeeResponseDtos = order.getOrderCoffees().stream()
//                        .map(orderCoffee ->
//                                orderCoffeeToOrderCoffeeResponseDto(orderCoffee))
//                                .collect(Collectors.toList());
//
//        orderResponseDto.setCreatedAt(order.getCreatedAt());

    /// /        바꾼걸 새로 선언했던 orderResponseDto에 담자.
//        orderResponseDto.setOrderCoffees(orderCoffeeResponseDtos);
//
//        return orderResponseDto;
//    };
    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

    OrderCoffeeResponseDto orderCoffeeToOrderCoffeeResponseDto(OrderCoffee orderCoffee);

    default OrderResponseDto orderToOrderResponseDto(Order order) {
//        반환할 orderResponseDto를 변수에 담아보자.
        OrderResponseDto orderResponseDto = new OrderResponseDto ();
        List<OrderCoffeeResponseDto> orderCoffeeResponseDtos = order.getOrderCoffees ().stream ()
                .map ( orderCoffee ->
                        {
                            OrderCoffeeResponseDto orderCoffeeResponseDto = new OrderCoffeeResponseDto (
                                    orderCoffee.getCoffee ().getCoffeeId (),
                                    orderCoffee.getCoffee ().getKorName (),
                                    orderCoffee.getCoffee ().getEngName (),
                                    orderCoffee.getCoffee ().getPrice (),
                                    orderCoffee.getQuantity () );
                            return orderCoffeeResponseDto;
                        }
                )
                .collect ( Collectors.toList () );

        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setMember(order.getMember());
//        List<OrderCoffeeResponseDto> orderCoffeeResponseDtos = order.getOrderCoffees ().stream ()
//                .map(orderCoffee ->
//                {
//                    orderCoffeeResponseDto.setCoffeeId ( orderCoffee.getCoffee ().getCoffeeId () );
//                    orderCoffeeResponseDto.setKorName ( orderCoffee.getCoffee ().getKorName () );
//                    orderCoffeeResponseDto.setEngName ( orderCoffee.getCoffee ().getEngName () );
//                    orderCoffeeResponseDto.setPrice ( orderCoffee.getCoffee ().getPrice () );
//                    return orderCoffeeResponseDto;
//                }
//                )
//                .collect( Collectors.toList());
        orderResponseDto.setOrderCoffees(orderCoffeeResponseDtos );
        orderResponseDto.setOrderStatus(order.getOrderStatus ());
        orderResponseDto.setCreatedAt(order.getCreatedAt ());
        return orderResponseDto;
    }
}
