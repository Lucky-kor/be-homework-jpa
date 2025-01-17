package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import com.springboot.member.entity.Stamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String name;
    private String phone;
    private Member.MemberStatus memberStatus;   // 추가된 부분
    private int stampCount;

    public MemberResponseDto(long memberId, String email, String name, String phone, Member.MemberStatus memberStatus) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.memberStatus = memberStatus;
    }

    // 추가된 부분
    public String getMemberStatus() {
        return memberStatus.getStatus();
    }

    public void setStamp(Stamp stamp) {
        this.stampCount = stamp.getStampCount();
    }
}
