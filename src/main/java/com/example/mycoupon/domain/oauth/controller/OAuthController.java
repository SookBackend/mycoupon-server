package com.example.mycoupon.domain.oauth.controller;

import com.example.mycoupon.domain.jwt.TokenProvider;
import com.example.mycoupon.domain.jwt.config.JwtConfig;
import com.example.mycoupon.domain.member.Member;
import com.example.mycoupon.domain.member.MemberService;
import com.example.mycoupon.domain.oauth.dto.KakaoLogoutDto;
import com.example.mycoupon.global.CallApiService;
import com.example.mycoupon.global.redis.RedisUtil;
import com.example.mycoupon.global.response.dto.ApiResponse;
import com.example.mycoupon.global.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final MemberService memberService;
    private final RedisUtil redisUtil;

    @GetMapping("/member/logout")
    public ApiResponse<?> logoutKakao(@RequestHeader("Authorization") String authHeader) {

        String accessToken = TokenProvider.getTokenFromHeader(authHeader);
        Map<String, Object> claim = TokenProvider.validateToken(accessToken);
        Long memberId = ((Integer) claim.get("id")).longValue();
        Member byId = memberService.findById(memberId);

        // 사용자 계정의 카카오 access token 가져와서 black list에 추가
        ClientResponse clientResponse = CallApiService.checkKakaoToken(byId.getSocialAccessToken()); // webflux
        redisUtil.setBlackList(accessToken,"accessToken",30); // 고유 값을 key로 저장


        // 카카오 access token이 유효하지 않은 경우
        if(clientResponse.statusCode() == HttpStatusCode.valueOf(401)){
            return ApiResponse.failure("400", "카카오 access token이 유효하지 않습니다.");
        }
        return ApiResponse.success(CallApiService.logoutKakao(byId, byId.getSocialAccessToken()),SuccessStatus.KAKAO_LOGOUT_SUCCESS);
    }

    @GetMapping("/member/resign")
    public ApiResponse<?> resignKakao(@RequestHeader("Authorization") String authHeader) {
        String accessToken = TokenProvider.getTokenFromHeader(authHeader);
        Map<String, Object> claim = TokenProvider.validateToken(accessToken);
        Long memberId = ((Integer) claim.get("id")).longValue();
        Member byId = memberService.findById(memberId);

        ClientResponse clientResponse = CallApiService.checkKakaoToken(byId.getSocialAccessToken());

        // 카카오 액세스 토큰이 만료됐다면
        if (clientResponse.statusCode() == HttpStatusCode.valueOf(401)){
            // 카카오 로그인창으로 리다이렉트 -> 액세스 토큰 재발급 받아야 함
            return ApiResponse.failure("440","카카오 액세스 토큰이 만료되었습니다. 로그인 후 재발급 받아주세요.");
        }

        KakaoLogoutDto kakaoLogoutDto = CallApiService.resignKakao(byId.getSocialAccessToken());
        memberService.deleteMember(memberId);
        return ApiResponse.success("200",SuccessStatus.KAKAO_RESIGN_SUCCESS);
    }
}
