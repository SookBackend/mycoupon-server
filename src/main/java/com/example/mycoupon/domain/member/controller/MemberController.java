package com.example.mycoupon.domain.member.controller;

import com.example.mycoupon.domain.jwt.TokenProvider;
import com.example.mycoupon.domain.member.controller.api.MemberApi;
import com.example.mycoupon.domain.member.entity.Member;
import com.example.mycoupon.domain.member.service.MemberService;
import com.example.mycoupon.global.response.dto.Response;
import com.example.mycoupon.global.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController implements MemberApi {

    private final MemberService memberService;

    // access token을 복호화하여 사용자 정보 응답
    @GetMapping("/member/info")
    public Response<?> getMemberInfo(@RequestHeader("Authorization") String authHeader) {

        String accessToken = TokenProvider.getTokenFromHeader(authHeader);
        Map<String, Object> claim = TokenProvider.validateToken(accessToken);
        Long memberId = ((Integer) claim.get("id")).longValue();
        Member result = memberService.findById(memberId);

        return Response.success(result, SuccessStatus.MEMBER_INFO_SUCCESS);
    }
}
