package com.example.mycoupon.domain.oauth.service;

import com.example.mycoupon.domain.member.entity.Member;
import com.example.mycoupon.domain.member.dto.MemberDto;
import com.example.mycoupon.domain.member.repository.MemberRepository;
import com.example.mycoupon.domain.member.entity.Role;
import com.example.mycoupon.domain.member.entity.UserPrincipal;
import com.example.mycoupon.domain.oauth.entity.KakaoOAuth2UserInfo;
import jakarta.transaction.Transactional;
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
import java.util.Map;
import java.util.Optional;

// 로그인 성공 이후 사용자 정보를 로드하고 객체를 생성
// http://localhost:8080/oauth2/authorization/kakao
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2AccessToken socialAccessToken = userRequest.getAccessToken();

        KakaoOAuth2UserInfo kakaoOAuth2UserInfo = new KakaoOAuth2UserInfo(attributes);
        String socialId = kakaoOAuth2UserInfo.getSocialId();
        String name = kakaoOAuth2UserInfo.getName();
        String email = kakaoOAuth2UserInfo.getEmail();
        String profileImageUrl = kakaoOAuth2UserInfo.getProfileImageUrl();

        Optional<Member> bySocialId = memberRepository.findBySocialId(socialId);
        log.info("Member found: {}", bySocialId.isPresent());

        Member member = bySocialId.orElseGet(()->saveSocialMember(socialId,name,email,profileImageUrl, socialAccessToken.getTokenValue()));
        updateSocialAccessToken(member, socialAccessToken.getTokenValue());
        MemberDto memberDto = new MemberDto(member.getId(), member.getEmail(), member.getName(), member.getSocialId(), member.getProfileImageUrl(), member.getRole());

        log.info("OAuth2 User Info: Social ID = {}, Name = {}, Email = {}, Profile Image URL = {}, Access Token = {}",
                socialId, name, email, profileImageUrl, socialAccessToken.getTokenValue());

        return new UserPrincipal(
                memberDto,
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())),
                attributes
        );
    }

    private Member saveSocialMember(String socialId, String name, String email, String profileImageUrl, String accessToken) {
        Member member = Member.builder()
                .name(name)
                .socialId(socialId)
                .email(email)
                .role(Role.USER)
                .profileImageUrl(profileImageUrl)
                .socialAccessToken(accessToken)
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember;

    }

    public void updateSocialAccessToken(Member member, String accessToken){
        member.updateSocialAccessToken(accessToken);
    }
}
