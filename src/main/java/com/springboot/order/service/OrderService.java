package com.springboot.order.service;

import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.service.CoffeeService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.order.dto.OrderCoffeeDto;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.entity.Order;
import com.springboot.order.repository.OrderRepository;
import com.springboot.order_coffee.entity.OrderCoffee;
import com.springboot.stamp.entity.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final CoffeeService coffeeService;

    public OrderService(MemberService memberService,
                        CoffeeService coffeeService,
                        OrderRepository orderRepository) {
        this.memberService = memberService;
        this.coffeeService = coffeeService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        // TODO 커피가 존재하는지 조회하는 로직이 포함되어야 합니다.
        // 회원이 존재하는 가
        Member member = memberService.findVerifiedMember(order.getMember().getMemberId());
        // 커피가 존재하는 가
        order.getOrderCoffees()
                .forEach(orderCoffee ->
                        coffeeService.findVerifiedCoffee(orderCoffee.getCoffee().getCoffeeId()));

        // 주문한 커피의 수량만큼 회원의 스탬프 숫자 증가
        int totalQuantity = order.getOrderCoffees().stream()
                .mapToInt(OrderCoffee -> OrderCoffee.getQuantity())
                .sum();

        member.getStamp().addStampCount(totalQuantity);

        return orderRepository.save(order);
    }

    // 메서드 추가
    public Order updateOrder(Order order) {
        Order findOrder = findVerifiedOrder(order.getOrderId());

        Optional.ofNullable(order.getOrderStatus())
                .ifPresent(orderStatus -> findOrder.setOrderStatus(orderStatus));
        findOrder.setModifiedAt(LocalDateTime.now());
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
