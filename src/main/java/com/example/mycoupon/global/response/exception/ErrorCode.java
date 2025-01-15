package com.example.mycoupon.global.response.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // jwt
    TEST_ERROR_CODE("400", "test"),
    EXPIRED_JWT_EXCEPTION("401","토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND("404","토큰이 전달되지 않았습니다."),
    INVALID_TOKEN_FORMAT("400","Bearer로 시작하지 않은 올바르지 않은 토큰 형식입니다."),
    INVALID_TOKEN("400","올바르지 않은 토큰입니다."),
    TOKEN_CACHE_NOT_FOUND("404","해당 유저 캐시에 refreshToken이 존재하지 않습니다."),

    // member
    MEMBER_NOT_FOUND("404","해당 멤버 객체를 찾을 수 없습니다."),
    COUPON_NOT_FOUND("404","해당 쿠폰 객체를 찾을 수 없습니다."),
    NO_AUTHORITY_COUPON("401","해당 멤버는 쿠폰에 대한 권한이 없습니다."),

    // coupon
    PAST_DATE_EXCEPTION("400","만료 날짜가 과거일 수 없습니다."),
    ;

    private final String code;
    private final String message;
}
