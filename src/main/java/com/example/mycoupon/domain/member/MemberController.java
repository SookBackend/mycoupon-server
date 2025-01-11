package com.example.mycoupon.domain.member;

import com.example.mycoupon.domain.jwt.TokenProvider;
import com.example.mycoupon.global.response.dto.ApiResponse;
import com.example.mycoupon.global.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // access token을 복호화하여 사용자 정보 응답
    @GetMapping("/member/info")
    public ApiResponse<?> getMemberInfo(@RequestHeader("Authorization") String authHeader) {

        String accessToken = TokenProvider.getTokenFromHeader(authHeader);
        Map<String, Object> claim = TokenProvider.validateToken(accessToken);
        Long memberId = ((Integer) claim.get("id")).longValue();
        Member result = memberService.findById(memberId);

        return ApiResponse.success(result, SuccessStatus.MEMBER_INFO_SUCCESS);
    }
}
