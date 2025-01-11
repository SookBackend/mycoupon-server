package com.example.mycoupon.domain.member.dto;

import com.example.mycoupon.domain.member.entity.Role;

public record MemberDto (
        Long id,
        String email,
        String name,
        String socialId,
        String profileImageUrl,
        Role role
){
}
