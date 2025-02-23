package com.example.mycoupon.global.security.config;

import com.example.mycoupon.global.redis.RedisUtil;
import com.example.mycoupon.global.security.filter.TokenAuthenticationFilter;
import com.example.mycoupon.global.security.handler.LoginSuccessHandler;
import com.example.mycoupon.domain.oauth.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oauth2UserService;
    private final RedisUtil redisUtil;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*")); // 협업 시 프론트 서버가 위치한 도메인
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","PATCH", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control")); // 허용할 HTTP 헤더
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 프론트 요청 허용

        return source;
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(redisUtil);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(redisUtil);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/",
                                "/swagger-ui/index.html", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**","/swagger-resources/**", "/v3/api-docs",
                                "/oauth2/login", "/login/oauth2/code/**", "/oauth2/authorization/**","/login/oauth2/code/google","/member/*/refresh"
                        ).permitAll() // 인증 없이 접근 가능
                        .anyRequest().authenticated() // 나머지 URL은 인증 필요
                )
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 토큰을 먼저 검증하고 사용자 인증 해야 함
                .formLogin(FormLoginConfigurer::disable) // 기본 로그인 폼 사용 안함
                .oauth2Login(customConfigurer -> customConfigurer
                        .successHandler(loginSuccessHandler())
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(oauth2UserService)
                        )
                )
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
