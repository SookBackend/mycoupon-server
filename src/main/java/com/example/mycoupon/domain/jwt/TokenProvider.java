package com.example.mycoupon.domain.jwt;

import ch.qos.logback.core.util.StringUtil;
import com.example.mycoupon.domain.member.Member;
import com.example.mycoupon.domain.member.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// jwt를 사용하여 사용자 인증과 권한 부여를 처리하는 클래스 -> 토큰 생성, 복호화, 검증
@RequiredArgsConstructor
@Component
@Slf4j
public class TokenProvider {

    @Value("${jwt.key}")
    private String key;
    private SecretKey secretKey;
    private static final Date ACCESS_TOKEN_EXPIRE_TIME = new Date(1000 * 60 * 30L);
    private static final Date REFRESH_TOKEN_EXPIRE_TIME = new Date(1000 * 60 * 60 * 24L);
    private final MemberRepository memberRepository;

    // jwt key 암호화 하여 초기화
    @PostConstruct
    private void initSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    // access token 발급
    public String generateAccessToken(Authentication authentication){
        // member의 권한 추출
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities) // member의 권한을 클레임에 추가
                .setExpiration(ACCESS_TOKEN_EXPIRE_TIME)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return accessToken;
    }

    // refresh token 발급
    public String generateRefreshToken(Authentication authentication){
        String refreshToken=Jwts.builder()
                .setExpiration(REFRESH_TOKEN_EXPIRE_TIME)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        SaveUpdateRefreshToken(authentication.getName(), refreshToken);
        return refreshToken;
    }

    // 사용자 객체에 refresh 토큰 저장
    public void SaveUpdateRefreshToken(String name, String refreshToken){
        Member member = memberRepository.findByName(name)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
    }

//    public String reissueToken(String accessToken){
//        // refresh 토큰 재발급 메소드
//    }

    public TokenDto generateToken(Authentication authentication){
        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(generateAccessToken(authentication))
                .refreshToken(generateRefreshToken(authentication))
                .build();
    }

    // jwt 복호화 -> 사용자 정보 추출
    public Authentication getAuthentication(String accessToken){
        Claims claims = parseClaims(accessToken);

        if(claims.get("auth") != null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails란? 인증 및 권한 부여에 필요한 사용자 정보를 제공
        // UserDetails 객체를 만들어서 Authentication 반환
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // access token 복호화
    private Claims parseClaims(String accessToken){
        try{
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody(); // jwt 토큰의 검증과 파싱을 수행
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    // 토큰 정보를 검증
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            log.info("expired token", e);
        }catch (SecurityException | MalformedJwtException e){
            log.info("invalid token", e);
        }catch(IllegalArgumentException e){
            log.info("claims string is empty", e);
        }catch (UnsupportedJwtException e){
            log.info("unsupported token", e);
        }
        return false;
    }


}
