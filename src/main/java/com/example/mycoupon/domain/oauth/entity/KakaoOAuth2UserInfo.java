package com.example.mycoupon.domain.oauth.entity;

import java.util.Map;

// 커스텀 사용자 정보 구체화
public class KakaoOAuth2UserInfo{
    public static String socialId;
    public static Map<String, Object> account;
    public static Map<String, Object> profile;
    public static String email;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        socialId = String.valueOf(attributes.get("id"));
        account = (Map<String, Object>) attributes.get("kakao_account");

        if (account != null) {
            profile = (Map<String, Object>) account.get("profile");
            email = (String) account.get("email");
        }
    }

    public String getSocialId() {
        return socialId;
    }

    public String getName() {
        return String.valueOf(profile.get("nickname"));
    }

    public String getEmail(){
        return email;
    }

    public String getProfileImageUrl() {
        return String.valueOf(profile.get("profile_image_url"));
    }

}
