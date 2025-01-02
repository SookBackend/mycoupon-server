package com.example.mycoupon.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    TEST_SUCCESS_CODE("200", "test");

    private final String code;
    private final String message;
}
