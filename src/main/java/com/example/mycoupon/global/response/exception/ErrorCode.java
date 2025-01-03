package com.example.mycoupon.global.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TEST_ERROR_CODE("400", "test");

    private final String code;
    private final String message;
}
