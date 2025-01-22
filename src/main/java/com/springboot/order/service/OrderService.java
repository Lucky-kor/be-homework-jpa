package com.springboot.order.service;

import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.repository.CoffeeRepository;
import com.springboot.coffee.service.CoffeeService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.order.entity.Order;
import com.springboot.order.repository.OrderRepository;
import com.springboot.orderCoffee.entity.OrderCoffee;
import com.springboot.orderCoffee.repository.OrderCoffeeRepository;
import org.hibernate.loader.plan.spi.CollectionFetchableElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final CoffeeService coffeeService;
    private final CoffeeRepository coffeeRepository;
    private final OrderCoffeeRepository orderCoffeeRepository;

    public OrderService(MemberService memberService,
                        OrderRepository orderRepository,
                        CoffeeService coffeeService,
                        CoffeeRepository coffeeRepository,
                        OrderCoffeeRepository orderCoffeeRepository
                        ) {
        this.memberService = memberService;
        this.orderRepository = orderRepository;
        this.coffeeService = coffeeService;
        this.coffeeRepository = coffeeRepository;
        this.orderCoffeeRepository = orderCoffeeRepository;

    }

    public Order createOrder(Order order) {
        // 회원이 존재하는지 확인
        memberService.findVerifiedMember(order.getMember().getMemberId());

        // TODO 커피가 존재하는지 조회하는 로직이 포함되어야 합니다.

        List<Long> coffeeIds = order.getOrderCoffees().stream()
                .map(orderCoffee -> orderCoffee.getCoffee().getCoffeeId())
                .collect(Collectors.toList());

        boolean isValid = coffeeIds.stream()
                .map(id -> coffeeService.findVerifiedCoffeeId(id))
                .anyMatch(result ->result);

        if(!isValid){
            new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND);
        }

        order.getOrderCoffees().forEach(orderCoffee -> orderCoffee.setOrder(order));


        return orderRepository.save(order);
    }

    // 메서드 추가
    public Order updateOrder(Order order) {
        Order findOrder = findVerifiedOrder(order.getOrderId());

        Optional.ofNullable(order.getOrderStatus())
                .ifPresent(orderStatus -> findOrder.setOrderStatus(orderStatus));
        findOrder.setModifiedAt(LocalDateTime.now());

//        findOrder.getMember().setStamp(order.getMember().getStamp());

        if(findOrder.getOrderStatus() == Order.OrderStatus.ORDER_CONFIRM) {
            Member member = memberService.findVerifiedMember(findOrder.getMember().getMemberId());
            findOrder.setMember(member);

            findOrder.getOrderCoffees().get(findOrder.getOrderId().intValue() -1).getQuantity();
            int totalQuantity = findOrder.getOrderCoffees().stream()
                    .mapToInt(orderCoffee -> orderCoffee.getQuantity())
                    .sum();
            findOrder.getMember().getStamp().setStampCount(totalQuantity);
        }

        return orderRepository.save(findOrder);
    }



    public Order findOrder(long orderId) {
        return findVerifiedOrder(orderId);
    }

    public Page<Order> findOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size,
                Sort.by("orderId").descending()));
    }

    public void cancelOrder(long orderId) {
        Order findOrder = findVerifiedOrder(orderId);
        int step = findOrder.getOrderStatus().getStepNumber();

        // OrderStatus의 step이 2 이상일 경우(ORDER_CONFIRM)에는 주문 취소가 되지 않도록한다.
        if (step >= 2) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }
        findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        findOrder.setModifiedAt(LocalDateTime.now());
        orderRepository.save(findOrder);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order findOrder =
                optionalOrder.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOrder;
    }


}
