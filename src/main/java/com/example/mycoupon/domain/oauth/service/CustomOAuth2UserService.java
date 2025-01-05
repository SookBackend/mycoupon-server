package com.example.mycoupon.domain.oauth.service;

import com.example.mycoupon.domain.member.Member;
import com.example.mycoupon.domain.member.MemberRepository;
import com.example.mycoupon.domain.oauth.entity.ProviderType;
import com.example.mycoupon.domain.oauth.entity.UserPrincipal;
import com.example.mycoupon.domain.oauth.info.OAuth2UserInfo;
import com.example.mycoupon.domain.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// 로그인 성공 이후 사용자 정보를 로드하고 객체를 생성
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth2 사용자 정보 엔드포인트 호출 -> 사용자 정보를 로드
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 소셜로그인 식별자 추출
        String ProviderTypeKey = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        ProviderType providerType = ProviderType.valueOf(ProviderTypeKey);

        // 로드한 사용자 정보가 담긴 속성 맵
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // OAuth2UserInfo를 통해 특정 Provider에 맞는 사용자 정보를 추출
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuthUserInfo(providerType,attributes);

        // 사용자 조회 시 providerId 사용
        String providerId = oAuth2UserInfo.getProviderId();

        // 사용자 정보를 조회 후 없는 경우 새로 생성
        Member member=getOrCreateMember(providerId,providerType,oAuth2UserInfo);

        // 권한 설정 (User)
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        //Security Context에 저장할 사용자 정보 반환
        return new UserPrincipal(member, attributes, authorities);


    }

    // DB에서 사용자 정보를 조회 후 없는 경우 새로 생성
    private Member getOrCreateMember(String providerId, ProviderType providerType, OAuth2UserInfo oAuth2UserInfo) {
        Optional<Member> member = memberRepository.findByProviderIdAndProviderType(providerId, providerType);

        return member.orElseGet(()->{
            Member newMember = Member.builder()
                    .name(oAuth2UserInfo.getName())
                    .providerId(providerId)
                    .providerType(providerType)
                    .profileImageUrl(oAuth2UserInfo.getProfileImageUrl())
                     .build();
            return memberRepository.save(newMember);
        });
    }
}
