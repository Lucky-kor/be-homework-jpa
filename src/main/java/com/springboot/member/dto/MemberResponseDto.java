package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import com.springboot.stamp.entity.Stamp;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {
    private long memberId;
    private String email;
    private String name;
    private String phone;
    private Member.MemberStatus memberStatus;   // 추가된 부분
    private int StampCount;

    // 추가된 부분
    public String getMemberStatus() {
        return memberStatus.getStatus();
    }
}
