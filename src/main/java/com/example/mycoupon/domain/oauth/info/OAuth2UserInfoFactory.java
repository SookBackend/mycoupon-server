package com.example.mycoupon.domain.oauth.info;

import com.example.mycoupon.domain.oauth.entity.ProviderType;

import java.util.Map;

// 다양한 OAuth2 provider에서 가져온 사용자 정보를 표준화 처리
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuthUserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType){
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
//            case NAVER: return new NaverOAuth2UserInfo(attributes);
//            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid provider type");
        }
    }
}
