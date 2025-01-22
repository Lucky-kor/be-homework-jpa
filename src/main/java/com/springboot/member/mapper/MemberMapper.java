package com.springboot.member.mapper;

import com.springboot.member.dto.MemberPatchDto;
import com.springboot.member.dto.MemberPostDto;
import com.springboot.member.dto.MemberResponseDto;
import com.springboot.member.entity.Member;
import com.springboot.stamp.Stamp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
//    Member memberPostDtoToMember(MemberPostDto memberPostDto);
    default Member memberPostDtoToMember(MemberPostDto memberPostDto) {
        Member member = new Member();
        member.setName(memberPostDto.getName());
        member.setPhone(memberPostDto.getPhone());
        member.setEmail(memberPostDto.getEmail());

        Stamp stamp = new Stamp();
        stamp.setMember(member);
        member.setStamp(stamp);

        return member;
    }
    Member memberPatchDtoToMember(MemberPatchDto memberPatchDto);

//    MemberResponseDto memberToMemberResponseDto(Member member);
    default MemberResponseDto memberToMemberResponseDto(Member member) {
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberId(member.getMemberId());
        memberResponseDto.setMemberStatus(member.getMemberStatus());
        memberResponseDto.setName(member.getName());
        memberResponseDto.setPhone(member.getPhone());
        memberResponseDto.setEmail(member.getEmail());
        memberResponseDto.setStamp(member.getStamp().getStampCount());

        return memberResponseDto;
    }

    List<MemberResponseDto> membersToMemberResponseDtos(List<Member> members);
}
