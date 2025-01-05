package com.example.mycoupon.domain.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    private String grantType; // Bearer
    private String accessToken;
    private String refreshToken;
}
