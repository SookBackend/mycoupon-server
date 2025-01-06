package com.example.mycoupon.domain.oauth.service;

import com.example.mycoupon.domain.member.Member;
import com.example.mycoupon.domain.member.MemberDto;
import com.example.mycoupon.domain.member.MemberRepository;
import com.example.mycoupon.domain.member.Role;
import com.example.mycoupon.domain.oauth.entity.UserPrincipal;
import com.example.mycoupon.domain.oauth.info.KakaoOAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// 로그인 성공 이후 사용자 정보를 로드하고 객체를 생성
// http://localhost:8080/oauth2/authorization/kakao
@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("loadUser method called");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2AccessToken accessToken=userRequest.getAccessToken();

        System.out.println("Kakao API Response: " + attributes);

        KakaoOAuth2UserInfo kakaoOAuth2UserInfo = new KakaoOAuth2UserInfo(attributes);
        String socialId = kakaoOAuth2UserInfo.getSocialId();
        String name = kakaoOAuth2UserInfo.getName();
        String email = kakaoOAuth2UserInfo.getEmail();
        String profileImageUrl = kakaoOAuth2UserInfo.getProfileImageUrl();

        Optional<Member> bySocialId = memberRepository.findBySocialId(socialId);
        log.info("Member found: {}", bySocialId.isPresent());
        Member member = bySocialId.orElseGet(()->saveSocialMember(socialId,name,email,profileImageUrl));
        MemberDto memberDto = new MemberDto(member.getId(), member.getEmail(), member.getName(), member.getSocialId(), member.getProfileImageUrl(), member.getRole());

        return new UserPrincipal(
                memberDto,
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())),
                attributes
        );
    }

    private Member saveSocialMember(String socialId, String name, String email, String profileImageUrl) {
        log.info("Saving member with socialId: {}, name: {}, email: {}, profileImageUrl: {}", socialId, name, email, profileImageUrl);
        Member member = Member.builder()
                .name(name)
                .socialId(socialId)
                .email(email)
                .role(Role.USER)
                .profileImageUrl(profileImageUrl)
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember;

    }
}
