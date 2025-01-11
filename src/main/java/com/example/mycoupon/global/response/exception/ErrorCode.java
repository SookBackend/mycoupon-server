package com.example.mycoupon.global.response.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEST_ERROR_CODE("400", "test"),
    EXPIRED_JWT_EXCEPTION("4001","토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND("404","토큰이 전달되지 않았습니다."),
    INVALID_TOKEN_FORMAT("400","Bearer로 시작하지 않은 올바르지 않은 토큰 형식입니다."),
    INVALID_TOKEN("400","올바르지 않은 토큰입니다."),
    MEMBER_NOT_FOUND("404","해당 멤버 객체를 찾을 수 없습니다."),
    TOKEN_CACHE_NOT_FOUND("404","해당 유저 캐시에 refreshToken이 존재하지 않습니다."),
    ;

    private final String code;
    private final String message;
}
