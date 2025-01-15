package com.example.mycoupon.domain.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ISSUED("ISSUED"), USED("USED"), EXPIRED("EXPIRED");
    private final String value;
}