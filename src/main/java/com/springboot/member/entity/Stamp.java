package com.springboot.member.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Stamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stampId;

    @Column(nullable = false)
    private int stampCount;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member){
        this.member = member;
        if(member.getStamp() != this){
            member.setStamp(this);
        }
    }
}
