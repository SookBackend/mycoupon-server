package com.example.mycoupon.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    TEST_SUCCESS_CODE("200", "test"),
    TOKEN_REFESH_SUCCESS("201", "토큰이 재발급 되었습니다."),
    MEMBER_INFO_SUCCESS("200","access token에 해당하는 멤버 정보입니다."),
    KAKAO_LOGOUT_SUCCESS("200","카카오 계정에서 로그아웃 되었습니다."),
    KAKAO_RESIGN_SUCCESS("200","카카오 계정이 탈퇴되었습니다.")
    ;

    private final String code;
    private final String message;
}
