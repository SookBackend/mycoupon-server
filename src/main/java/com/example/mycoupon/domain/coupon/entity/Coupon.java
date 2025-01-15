package com.example.mycoupon.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private Long issuerId;

    @Convert(converter = LongListConverter.class)
    private List<Long> recipientIds = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate expirationDate; // 만료일

    private LocalDate createdDate; // 생성일

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

}
