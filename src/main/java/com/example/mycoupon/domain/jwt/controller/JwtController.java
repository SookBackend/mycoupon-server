package com.example.mycoupon.domain.jwt.controller;

import com.example.mycoupon.domain.jwt.controller.api.JwtApi;
import com.example.mycoupon.domain.jwt.dto.RefreshDto;
import com.example.mycoupon.domain.jwt.TokenProvider;
import com.example.mycoupon.domain.jwt.config.JwtConfig;
import com.example.mycoupon.domain.member.entity.Member;
import com.example.mycoupon.domain.member.service.MemberService;
import com.example.mycoupon.global.redis.RedisUtil;
import com.example.mycoupon.global.response.dto.Response;
import com.example.mycoupon.global.response.dto.SuccessStatus;
import com.example.mycoupon.global.response.exception.CustomException;
import com.example.mycoupon.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JwtController implements JwtApi {

    private final MemberService memberService;
    private final RedisUtil redisUtil;

    // 헤더에 access token(x) refresh token(O)
    public Response<?> refresh(@RequestHeader("Authorization") String authHeader) {

        String refreshToken= TokenProvider.getTokenFromHeader(authHeader);
        Map<String, Object> claim = TokenProvider.validateToken(refreshToken);
        Long memberId = ((Integer) claim.get("id")).longValue();
        Member byId = memberService.findById(memberId);

        Object o = redisUtil.get(byId.getEmail());

        if (o == null) {
            throw new CustomException(ErrorCode.TOKEN_CACHE_NOT_FOUND);
        }
        String redisRefreshToken = o.toString();

        if (redisRefreshToken.equals(refreshToken)){
           redisUtil.delete(byId.getEmail());

            String newAccessToken = TokenProvider.generateToken(claim, JwtConfig.ACCESS_EXP_TIME);
            String newRefreshToken = TokenProvider.generateToken(claim, JwtConfig.REFRESH_EXP_TIME);

            redisUtil.set(byId.getEmail(), newRefreshToken, 60*24L);

            RefreshDto result = new RefreshDto(newAccessToken, newRefreshToken);
            return Response.success(result, SuccessStatus.TOKEN_REFESH_SUCCESS);
        }

        return Response.failure("500","토큰 재발급에 실패했습니다.");
    }
}
