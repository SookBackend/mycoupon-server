package com.example.mycoupon.global.response.dto;

import com.example.mycoupon.global.response.dto.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public static <T> ApiResponse<T> success(T result, SuccessStatus status) {
        return new ApiResponse<>(true, status.getCode(), status.getMessage(), result);
    }

    public static ApiResponse<Void> success(SuccessStatus status) {
        return new ApiResponse<>(true, status.getCode(), status.getMessage(), null);
    }

    public static ApiResponse<Void> failure(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
