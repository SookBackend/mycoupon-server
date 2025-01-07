package com.example.mycoupon.domain.oauth.entity;

import com.example.mycoupon.domain.member.Member;
import com.example.mycoupon.domain.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// SecurityContext에 저장할 커스텀 사용자 객체 규격
@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User {

    private final MemberDto memberDto;
    private Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(MemberDto memberDto, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.memberDto = memberDto;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    public UserPrincipal(MemberDto memberDto, Collection<? extends GrantedAuthority> authorities) {
        this.memberDto = memberDto;
        this.authorities = authorities;
    }

    public Map<String, Object> getMemberInfo(){
        Map<String, Object> info = new HashMap<>();
        info.put("id", memberDto.id());
        info.put("name", memberDto.name());
        info.put("email", memberDto.email());
        info.put("role", memberDto.role().getValue());
        info.put("profileImageUrl", memberDto.profileImageUrl());
        info.put("socialId", memberDto.socialId());
        return info;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberDto.name();
    }

    @Override
    public String getName() {
        return memberDto.email();
    }
}
