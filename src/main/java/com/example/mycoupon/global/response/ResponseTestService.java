package com.example.mycoupon.global.response;

import com.example.mycoupon.global.response.exception.CustomException;
import com.example.mycoupon.global.response.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class  ResponseTestService {
    public String getTestData(boolean shouldThrowError) {
        if (shouldThrowError) {
            throw new CustomException(ErrorCode.TEST_ERROR_CODE);
        }
        return "Test data";
    }
}
