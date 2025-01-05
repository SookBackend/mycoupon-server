package com.example.mycoupon.domain.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// jwt로 username, pwd 인증을 수행
// UsernamePasswordAuthenticationFilter 이전에 실행
// 클라이언트 요청 jwt가 유효하다면 토큰의 Authentication을 SecurityContext에 저장 -> 인증된 요청 처리 가능
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public final TokenProvider tokenProvider;

    private static final List<String> EXCLUDED_URLS = List.of(
            "/test",
            "/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/v3/api-docs"
//            ,"/webjars/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String accessToken = resolveToken(request);

        // 요청 URL이 제외된 URL 목록에 포함되어 있다면 필터 건너뛰기
        if (isExcludedUrl(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // access 토큰 유효성 검사: 참 -> Authentication 객체를 가져와 SecurityContext에 인증 정보 저장
        if(tokenProvider.validateToken(accessToken)){
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
//        else{
//            // 토큰 재발급 코드 짜기 -> tokenProvider.reissueToken
//        }
        filterChain.doFilter(request, response);
    }

    // request-header에서 access 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isExcludedUrl(String requestUri) {
        return EXCLUDED_URLS.stream()
                .anyMatch(pattern -> requestUri.matches(pattern.replace("**", ".*"))); // ** -> .* 로 치환
    }
}