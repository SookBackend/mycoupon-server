package com.example.mycoupon.domain.oauth.entity;

import com.example.mycoupon.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// SecurityContext에 저장할 커스텀 사용자 객체 규격
@Getter
@AllArgsConstructor
public class UserPrincipal implements OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes;
    private final Collection<GrantedAuthority> authorities;


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return member.getName();
    }
}
