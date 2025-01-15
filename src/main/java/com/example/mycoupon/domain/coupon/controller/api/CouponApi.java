package com.example.mycoupon.domain.coupon.controller.api;

import com.example.mycoupon.domain.coupon.dto.CouponRequestDto;
import com.example.mycoupon.domain.coupon.entity.Coupon;
import com.example.mycoupon.global.response.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Coupon", description = "쿠폰 관리 API")
public interface CouponApi {

    @Operation(
            summary = "쿠폰 생성",
            description = "사용자의 쿠폰을 생성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponse(responseCode = "201", description = "쿠폰이 생성되었습니다.")
    @ApiResponse(responseCode = "400", description = "만료 날짜가 과거일 수 없습니다.")
    @ApiResponse(responseCode = "404", description = "해당 멤버 객체를 찾을 수 없습니다.")
    @PostMapping("/member/{memberId}/coupon")
    public Response<Coupon> createCoupon(@PathVariable Long memberId, @RequestBody CouponRequestDto couponRequestDto);


    @Operation(
            summary = "쿠폰 수정",
            description = "사용자의 쿠폰을 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponse(responseCode = "200", description = "쿠폰이 수정되었습니다.")
    @ApiResponse(responseCode = "400", description = "만료 날짜가 과거일 수 없습니다.")
    @ApiResponse(responseCode = "401", description = "해당 멤버는 쿠폰에 대한 권한이 없습니다.")
    @ApiResponse(responseCode = "404", description = "해당 객체를 찾을 수 없습니다.")
    @PutMapping("/member/{memberId}/coupon/{couponId}")
    public Response<Coupon> updateCoupon(@PathVariable Long memberId, @PathVariable Long couponId, @RequestBody CouponRequestDto couponRequestDto);


    @Operation(
            summary = "쿠폰 삭제",
            description = "사용자의 쿠폰을 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponse(responseCode = "204", description = "쿠폰이 삭제되었습니다.")
    @ApiResponse(responseCode = "401", description = "해당 멤버는 쿠폰에 대한 권한이 없습니다.")
    @ApiResponse(responseCode = "404", description = "해당 객체를 찾을 수 없습니다.")
    @DeleteMapping("/member/{memberId}/coupon/{couponId}")
    public Response<?> deleteCoupon(@PathVariable Long memberId, @PathVariable Long couponId);


    @Operation(
            summary = "쿠폰 조회",
            description = "사용자의 쿠폰을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization", required = true,
            schema = @Schema(type = "string"),
            description = "Bearer [Access 토큰]"
    )
    @ApiResponse(responseCode = "200", description = "쿠폰이 조회되었습니다.")
    @ApiResponse(responseCode = "404", description = "해당 객체를 찾을 수 없습니다.")
    @GetMapping("/member/{memberId}/coupon/")
    public Response<List<Coupon>> getAllCoupon(@PathVariable Long memberId);
}
