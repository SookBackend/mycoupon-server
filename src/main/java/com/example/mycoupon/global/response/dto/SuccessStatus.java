package com.example.mycoupon.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    TEST_SUCCESS_CODE("200", "test"),
    CREATED("201", "created"),
    TOKEN_REFESH_SUCCESS("202", "access token이 재발급 되었습니다."),
    MEMBER_INFO_SUCCESS("200","access token에 해당하는 멤버 정보입니다."),
    ;

    private final String code;
    private final String message;
}
