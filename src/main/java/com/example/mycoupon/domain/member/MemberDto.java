package com.example.mycoupon.domain.member;

public record MemberDto (
        Long id,
        String email,
        String name,
        String socialId,
        String profileImageUrl,
        Role role
){
}
