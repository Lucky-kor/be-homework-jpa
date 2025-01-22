package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import com.springboot.stamp.Stamp;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String name;
    private String phone;
    private Member.MemberStatus memberStatus;   // 추가된 부분
    private int stamp;

    // 추가된 부분
    public String getMemberStatus() {
        return memberStatus.getStatus();
    }
}
