package com.example.mycoupon.domain.jwt.dto;

public record RefreshDto (
        String accessToken,
        String refreshToken
) {
}
