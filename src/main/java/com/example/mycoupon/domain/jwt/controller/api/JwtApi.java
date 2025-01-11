package com.example.mycoupon.domain.jwt.controller.api;

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

@Tag(name = "jwt token 재발급", description = "액세스 토큰 기한 만료 됐을 때 호출 하는 API")
public interface JwtApi {
    @Operation(
            summary = "access token 만료 시 토큰 재발급을 위한 api",
            description = "access token(X) refresh token(O) 새로운 access, refresh token을 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Refresh 토큰]"
    )
    @ApiResponse(responseCode = "201", description = "토큰 재발급 성공")
    @ApiResponse(responseCode = "500", description = "토큰 재발급에 실패했습니다.")
    @ApiResponse(responseCode = "404", description = "해당 유저 캐시에 refreshToken이 존재하지 않습니다.")
    @GetMapping("/member/{id}/refresh")
    public Response<?> refresh(@RequestHeader("Authorization") String authHeader);
}
