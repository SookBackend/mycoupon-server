package com.example.mycoupon.global.security.handler;

import com.example.mycoupon.domain.jwt.JwtConfig;
import com.example.mycoupon.domain.jwt.TokenProvider;
import com.example.mycoupon.domain.oauth.entity.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

// 인증코드 성공 시 특정 페이지로 리다이렉트
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // 인증된 사용자 정보 가져오기
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        log.info("authentication.getPrincipal() = {}", userPrincipal);

        // access, refresh 토큰 생성
        String accessToken = TokenProvider.generateToken(userPrincipal.getMemberInfo(), JwtConfig.ACCESS_EXP_TIME);
        String refreshToken = TokenProvider.generateToken(userPrincipal.getMemberInfo(), JwtConfig.REFRESH_EXP_TIME);

        Long memberId = userPrincipal.getMemberDto().id();
        String redirectUri = "http://localhost:8080/auth";

        String redirectUrl = String.format("%s?member_id=%d&access_token=%s&refresh_token=%s", redirectUri, memberId, accessToken, refreshToken);
        response.sendRedirect(redirectUrl);


    }
}
