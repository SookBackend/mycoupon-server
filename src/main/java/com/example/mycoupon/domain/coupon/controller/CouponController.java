package com.example.mycoupon.domain.coupon.controller;

import com.example.mycoupon.domain.coupon.service.ConponService;
import com.example.mycoupon.domain.coupon.controller.api.CouponApi;
import com.example.mycoupon.domain.coupon.dto.CouponRequestDto;
import com.example.mycoupon.domain.coupon.entity.Coupon;
import com.example.mycoupon.domain.member.service.MemberService;
import com.example.mycoupon.global.response.dto.Response;
import com.example.mycoupon.global.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouponController implements CouponApi {

    private final ConponService conponService;
    private final MemberService memberService;

    public Response<Coupon> createCoupon(@PathVariable Long memberId, @RequestBody CouponRequestDto couponRequestDto) {
        Coupon coupon = conponService.createCoupon(memberId, couponRequestDto);
        return Response.success(coupon, SuccessStatus.COUPON_CREATED);
    }

    public Response<Coupon> updateCoupon(@PathVariable Long memberId, @PathVariable Long couponId, @RequestBody CouponRequestDto couponRequestDto) {
        Coupon coupon = conponService.updateCoupon(memberId,couponId, couponRequestDto);
        return Response.success(coupon, SuccessStatus.COUPON_UPDATED);
    }

    public Response<?> deleteCoupon(@PathVariable Long memberId, @PathVariable Long couponId) {
        conponService.deleteCoupon(memberId, couponId);
        return Response.success(SuccessStatus.COUPON_DELETED);
    }

    public Response<List<Coupon>> getAllCoupon(@PathVariable Long memberId) {
        return Response.success(conponService.getAllCoupon(memberId), SuccessStatus.COUPON_FETCHED);
    }

}
