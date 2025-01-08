package com.example.mycoupon.global.security.filter;

import com.example.mycoupon.domain.jwt.config.JwtConfig;
import com.example.mycoupon.domain.jwt.TokenProvider;
import com.example.mycoupon.global.redis.RedisUtil;
import com.example.mycoupon.global.response.exception.CustomException;
import com.example.mycoupon.global.response.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// jwt로 username, pwd 인증을 수행
// UsernamePasswordAuthenticationFilter 이전에 실행
// 클라이언트 요청 jwt가 유효하다면 토큰의 Authentication을 SecurityContext에 저장 -> 인증된 요청 처리 가능
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtil redisUtil;
    private static final String[] whitelist = {
            // login
            "/signUp", "/login", "/refresh", "/", "/index.html", "/oauth2/login", "/login/oauth2/code/*", "/oauth2/authorization/kakao","/member/*/refresh",
            //Swagger
            "/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/v3/api-docs",
            //ALB
            "/member/check"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(JwtConfig.JWT_HEADER);
        log.debug("Received Authorization Header: {}", authHeader);

        try{
            checkAuthorizationHeader(authHeader);
            String token = TokenProvider.getTokenFromHeader(authHeader);
            log.debug("Extracted Token: {}", token);

           Authentication authentication = TokenProvider.getAuthentication(token);
            log.debug("Authentication: {}", authentication);
           SecurityContextHolder.getContext().setAuthentication(authentication);
           filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    // jwt가 포함된 올바른 Authorization 형식인지 검증
    private void checkAuthorizationHeader(String header) {
        if (header==null){
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        } else if (!header.startsWith(JwtConfig.JWT_TYPE)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_FORMAT);
        }
    }

}