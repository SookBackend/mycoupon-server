package com.example.mycoupon.domain.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CouponRequestDto {

    @NotBlank
    private String title;

    private String content;

    private String imageUrl;

    @NotNull
    private Long issuerId;

    @NotNull
    private List<Long> recipientIds;

    private LocalDate expirationDate;
}
