package com.example.mycoupon.domain.jwt.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {
    public static String key;
    public static final int ACCESS_EXP_TIME = 30;
    public static final int REFRESH_EXP_TIME = 60 * 24;
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_TYPE = "Bearer ";

    @Value("${jwt.key}")
    private String jwtSecretKey;

    @PostConstruct
    public void init() {
        key = this.jwtSecretKey;
    }
}
