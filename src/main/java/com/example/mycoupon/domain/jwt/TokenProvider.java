package com.example.mycoupon.domain.jwt;

import com.example.mycoupon.domain.jwt.config.JwtConfig;
import com.example.mycoupon.domain.member.dto.MemberDto;
import com.example.mycoupon.domain.member.entity.Role;
import com.example.mycoupon.domain.member.entity.UserPrincipal;
import com.example.mycoupon.global.response.exception.CustomException;
import com.example.mycoupon.global.response.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;

// jwt를 사용하여 사용자 인증과 권한 부여를 처리하는 클래스 -> 토큰 생성, 복호화, 검증
@Slf4j
public class TokenProvider {

    public static String secretKey = JwtConfig.key;

    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    // 토큰 생성
    public static String generateToken(Map<String, Object> userPrincipal, long validTime) {

        SecretKey key = null;

        try{
            key = Keys.hmacShaKeyFor(TokenProvider.secretKey.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return Jwts.builder()
                .setHeader(Map.of("type","JWT"))
                .setClaims(userPrincipal)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(validTime).toInstant()))
                .signWith(key)
                .compact();
    }

    // 사용자 인증 객체 생성
    public static Authentication getAuthentication(String token) {
        Map<String, Object> claims = validateToken(token);

        // 클레임에서 사용자 정보 추출
        Long id = ((Integer) claims.get("id")).longValue();
        String name = (String) claims.get("name");
        String stRole = (String) claims.get("role");
        Role role = Role.valueOf(stRole);
        String email = (String) claims.get("email");
        String profileImageUrl = (String) claims.get("profile_image_url");
        String socialId = (String) claims.get("social_id");


        // User 객체 생성
        MemberDto memberDto = new MemberDto(id, email, name, socialId, profileImageUrl, role);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(memberDto.role().getValue()));
        UserPrincipal userPrincipal = new UserPrincipal(memberDto, authorities);

        // User 객체로 만든 Authentication 객체를 반환
        return new UsernamePasswordAuthenticationToken(userPrincipal, "", authorities);
    }

    // 토큰이 유효한지 검증
    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try{
            SecretKey key = Keys.hmacShaKeyFor(TokenProvider.secretKey.getBytes(StandardCharsets.UTF_8));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            throw new CustomException(ErrorCode.EXPIRED_JWT_EXCEPTION);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_FORMAT);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return claim;
    }

    public static boolean isExpired(String token) {
        try {
            validateToken(token);
        } catch (CustomException e) {
            return e.getErrorCode() == ErrorCode.EXPIRED_JWT_EXCEPTION;
        }
        return false;
    }

    public static long tokenRemainTime(Long expiredTime) {
        Date expiredDate = new Date(expiredTime * (1000));
        long remainMs = expiredDate.getTime() - System.currentTimeMillis();
        return remainMs / (1000 * 60);
    }



}
