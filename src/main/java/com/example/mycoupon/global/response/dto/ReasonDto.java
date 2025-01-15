package com.example.mycoupon.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReasonDto {
    private final String code;
    private final String message;
}
