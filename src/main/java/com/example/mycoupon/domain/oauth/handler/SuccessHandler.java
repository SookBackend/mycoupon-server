package com.example.mycoupon.domain.oauth.handler;

import com.example.mycoupon.domain.jwt.TokenDto;
import com.example.mycoupon.domain.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

// 인증코드 성공 시 특정 페이지로 리다이렉트
@RequiredArgsConstructor
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private static final String URI="test/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // access token, refresh token 발급
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        // 리다이렉트로 토큰 전달
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken",tokenDto.getAccessToken())
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
