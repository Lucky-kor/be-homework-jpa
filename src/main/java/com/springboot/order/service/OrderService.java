package com.springboot.order.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.entity.Stamp;
import com.springboot.member.service.MemberService;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import com.springboot.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;

    public OrderService(MemberService memberService,
                        OrderRepository orderRepository) {
        this.memberService = memberService;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        // 회원이 존재하는지 확인
        memberService.findVerifiedMember(order.getMember().getMemberId());

        updateStamp(order);

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

    public void updateStamp(Order order) {
        Member member = memberService.findVerifiedMember(order.getMember().getMemberId());
        Stamp stamp = member.getStamp();
        //       필요한 변수 - 현재 주문한 멤버의 수량
//       주문한 커피의 수량
//       그것을 더한 수량이 총 스탬프의 개수 -> 다시 DB에 저장
//       멤버안에 스템프객체가 들어있음.
        int currentStamp = order.getMember().getStamp().getStampCount();

//        추가로 찍어야할 스템프의 개수는 받은 주문의 커피의 수량이어야함. 스트림안에는 orderCoffee 의 quantity가 들어가야함.
        int addStamp = order.getOrderCoffeeList()
                .stream()
                .mapToInt(OrderCoffee::getQuantity)
                .sum();

//        두 개를 더해서 order안에 Member 안에 stamp에 넣어주면 된다.
        stamp.setStampCount(currentStamp + addStamp);
        stamp.setModifiedAt(LocalDateTime.now());

        memberService.updateMember(order.getMember());
    }
}
