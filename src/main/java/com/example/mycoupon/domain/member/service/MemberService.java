package com.example.mycoupon.domain.member.service;

import com.example.mycoupon.domain.member.repository.MemberRepository;
import com.example.mycoupon.domain.member.entity.Member;
import com.example.mycoupon.global.response.exception.CustomException;
import com.example.mycoupon.global.response.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Boolean deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.deleteById(id);
        return true;
    }
}
