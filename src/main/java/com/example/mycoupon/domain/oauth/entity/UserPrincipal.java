package com.example.mycoupon.domain.oauth.entity;

import com.example.mycoupon.domain.member.Member;
import com.example.mycoupon.domain.member.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

// SecurityContext에 저장할 커스텀 사용자 객체 규격
@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User {

    private final MemberDto memberDto;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(MemberDto memberDto, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.memberDto = memberDto;
        this.authorities = authorities;
        this.attributes = attributes;
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
