package com.example.mycoupon.domain.jwt;

public record RefreshDto (
        String accessToken,
        String refreshToken
) {
}
