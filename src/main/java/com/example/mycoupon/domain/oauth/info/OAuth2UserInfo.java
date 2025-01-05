package com.example.mycoupon.domain.oauth.info;

import java.util.Map;

// 커스텀 사용자 정보를 위한 캡슐화
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getName();
    public abstract String getProviderId();
    public abstract String getProfileImageUrl();
}
