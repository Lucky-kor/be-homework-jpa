package com.springboot.member.service;

import com.springboot.member.entity.Member;
import com.springboot.member.entity.Stamp;
import com.springboot.member.repository.MemberRepository;
import com.springboot.order.entity.Order;

public class StampService {

    private MemberRepository memberRepository;

    public StampService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public int updateStamps(Order order) {
        Member member = memberRepository.findById(order.getMember().getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));

        int additionalStamps = order.getOrderCoffees().stream()
                .mapToInt(orderCoffee -> orderCoffee.getQuantity())
                .sum();

        if (member.getStamp() == null) {
            member.setStamp(new Stamp());
        }

        Stamp stamp = member.getStamp();
        stamp.setStampCount(stamp.getStampCount() + additionalStamps);

        memberRepository.save(member);

        return additionalStamps;
    }
}