package com.example.mycoupon.domain.coupon.service;

import com.example.mycoupon.domain.coupon.repository.CouponRepository;
import com.example.mycoupon.domain.coupon.dto.CouponRequestDto;
import com.example.mycoupon.domain.coupon.entity.Status;
import com.example.mycoupon.domain.coupon.entity.Coupon;
import com.example.mycoupon.domain.member.entity.Member;
import com.example.mycoupon.domain.member.repository.MemberRepository;
import com.example.mycoupon.global.response.exception.CustomException;
import com.example.mycoupon.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    public Coupon createCoupon(Long memberId, CouponRequestDto couponRequestDto){

        // 존재하지 않은 member가 coupon 생성 요청한다면 에러 발생
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 쿠폰 만료일이 과거라면 에러 발생
        LocalDate now = LocalDate.now();
        if (couponRequestDto.getExpirationDate().isBefore(now)) {
            throw new CustomException(ErrorCode.PAST_DATE_EXCEPTION);
        }

        Coupon coupon = Coupon.builder()
                .title(couponRequestDto.getTitle())
                .content(couponRequestDto.getContent())
                .imageUrl(couponRequestDto.getImageUrl())
                .issuerId(memberId)
                .recipientIds(couponRequestDto.getRecipientIds())
                .status(Status.ISSUED)
                .expirationDate(couponRequestDto.getExpirationDate())
                .build();
        couponRepository.save(coupon);

        return coupon;
    }

    public Coupon updateCoupon(Long memberId, Long couponId, CouponRequestDto couponRequestDto){

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        // 수정자 memverId가 couponId의 쿠폰 작성자가 아니라면 에러
        if(!coupon.getIssuerId().equals(memberId)){
            throw new CustomException(ErrorCode.NO_AUTHORITY_COUPON);
        }

        // 쿠폰 만료일이 과거라면 에러 발생
        LocalDate now = LocalDate.now();
        if (couponRequestDto.getExpirationDate().isBefore(now)) {
            throw new CustomException(ErrorCode.PAST_DATE_EXCEPTION);
        }

        coupon.setTitle(couponRequestDto.getTitle());
        coupon.setContent(couponRequestDto.getContent());
        coupon.setImageUrl(couponRequestDto.getImageUrl());
        coupon.setRecipientIds(couponRequestDto.getRecipientIds());
        coupon.setExpirationDate(couponRequestDto.getExpirationDate());
        couponRepository.save(coupon);

        return coupon;
    }

    public void deleteCoupon(Long memberId, Long couponId){

        // member와 coupon 객체의 유효성 검사 -> 없는 객체라면 에러 발생
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

        // 수정자 memverId가 couponId의 쿠폰 작성자가 아니라면 에러
        if(!coupon.getIssuerId().equals(memberId)){
            throw new CustomException(ErrorCode.NO_AUTHORITY_COUPON);
        }

        couponRepository.delete(coupon);
    }

    public List<Coupon> getAllCoupon(Long memberId){

        // member와 coupon 객체의 유효성 검사 -> 없는 객체라면 에러 발생
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return couponRepository.findByIssuerId(memberId);
    }
}
