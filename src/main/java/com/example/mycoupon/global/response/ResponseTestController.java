package com.example.mycoupon.global.response;

import com.example.mycoupon.global.response.dto.ApiResponse;
import com.example.mycoupon.global.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResponseTestController {

    private final ResponseTestService responseTestService;

    @GetMapping("/res/test")
    public ApiResponse<String> getTest(@RequestParam boolean throwError) {
        String data = responseTestService.getTestData(throwError);
        return ApiResponse.success(data, SuccessStatus.TEST_SUCCESS_CODE);
    }
}
