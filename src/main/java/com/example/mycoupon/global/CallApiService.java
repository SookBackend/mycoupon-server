package com.example.mycoupon.global;

import com.example.mycoupon.domain.member.Member;
import com.example.mycoupon.domain.oauth.dto.KakaoLogoutDto;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

// 외부 API 호출 관련 기능 구현
public class CallApiService {

    private final WebClient webClient;

    public CallApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public static ClientResponse checkKakaoToken(String kakaoAccessToken){
        WebClient webClient = WebClient.builder().build();

        return webClient
                .get()
                .uri("https://kapi.kakao.com/v1/user/access_token_info")
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .exchange()
                .block();
    }

    public static KakaoLogoutDto logoutKakao(Member member, String kakaoAccessToken){
        WebClient webClient = WebClient.builder().build();

        return webClient
                .post()
                .uri("https://kapi.kakao.com/v1/user/logout")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoLogoutDto.class)
                .block();
    }

    public static KakaoLogoutDto resignKakao(String kakaoAccessToken){
        WebClient webClient = WebClient.builder().build();

        return webClient
                .post()
                .uri("https://kapi.kakao.com/v1/user/unlink")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization","Bearer "+kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoLogoutDto.class)
                .block();
    }

}
