package com.example.mycoupon.domain.member.controller.api;

import com.example.mycoupon.global.response.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "소셜 계정 정보 조회", description = "로그인한 사용자의 정보를 조회하는 API")
public interface MemberApi {

    @Operation(
            summary = "카카오 계정 사용자 정보압나다",
            description = "사용자 정보"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponse(responseCode = "200", description = "access token에 해당하는 멤버 정보입니다.")
    @GetMapping("/member/info")
    public Response<?> getMemberInfo(@RequestHeader("Authorization") String authHeader);
}
