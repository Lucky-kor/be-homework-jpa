package com.springboot.member.entity;

import com.springboot.order.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 13, nullable = false, unique = true)
    private String phone;

    // 추가된 부분
    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    // 멤버를 저장할 때 스탬프도 넣게
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Stamp stamp = new Stamp();

    public void setStamp(Stamp stamp){
        this.stamp = stamp;

        if(stamp.getMember() != this)
            stamp.setMember(this);
    }

    public Member(String email) {
        this.email = email;
    }

    public Member(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public void addOrder(Order order){
        //order의 member에 나 자신을 넣기
        //항상 이게 먼저 실행되어야함!!!!
        //둘다 위에 있으면 안됨!!
        orders.add(order);

        if (order.getMember() != this)
            order.setMember(this);
    }

    public void addStamp(Stamp stamp){ this.stamp = stamp;}

    // 추가 된 부분
    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
           this.status = status;
        }
    }
}
