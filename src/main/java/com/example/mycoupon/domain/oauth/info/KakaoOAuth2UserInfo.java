package com.example.mycoupon.domain.oauth.info;

import java.util.Map;

// 커스텀 사용자 정보 구체화
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return attributes.get("id").toString();
    }

    // Kakao의 attributes의 예시 참조
    @Override
    public String getProviderId() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null) {return null;}
        return (String) properties.get("nickname");
    }

    // Kakao의 attributes의 예시 참조
    @Override
    public String getProfileImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null) {return null;}
        return (String) properties.get("profile_image");
    }

}
