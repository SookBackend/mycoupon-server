package com.example.mycoupon.domain.oauth.api;

import com.example.mycoupon.domain.oauth.dto.KakaoLogoutDto;
import com.example.mycoupon.global.response.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "소셜 계정 로그아웃, 회원 탈퇴", description = "카카오 로그아웃, 회원 탈퇴 하는 API")
public interface OAuthApi {
    @Operation(
            summary = "카카오 로그아웃 api 입니다",
            description = "카카오 로그아웃",
            security = @SecurityRequirement(name = "")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponse(responseCode = "200", description = "카카오 계정에서 로그아웃 되었습니다.")
    @ApiResponse(responseCode = "401", description = "카카오 access token이 유효하지 않습니다.")
    @GetMapping("/member/logout")
    public Response<?> logoutKakao(@RequestHeader("Authorization") String accessToken);

    @Operation(
            summary = "카카오 계정 탈퇴 api 입니다",
            description = "카카오 탈퇴",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponse(responseCode = "200", description = "카카오 계정이 탈퇴되었습니다.")
    @ApiResponse(responseCode = "401", description = "카카오 access token이 유효하지 않습니다.")
    @GetMapping("/member/resign")
    public Response<?> resignKakao(@RequestHeader("Authorization") String accessToken);
}
