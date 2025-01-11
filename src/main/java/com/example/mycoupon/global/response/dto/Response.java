package com.example.mycoupon.global.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
    private final boolean isSuccess;
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    public static <T> Response<T> success(T result, SuccessStatus status) {
        return new Response<>(true, status.getCode(), status.getMessage(), result);
    }

    public static Response<Void> success(SuccessStatus status) {
        return new Response<>(true, status.getCode(), status.getMessage(), null);
    }

    public static Response<Void> failure(String code, String message) {
        return new Response<>(false, code, message, null);
    }
}
