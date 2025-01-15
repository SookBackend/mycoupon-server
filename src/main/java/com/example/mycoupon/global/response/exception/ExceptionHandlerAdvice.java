package com.example.mycoupon.global.response.exception;

import com.example.mycoupon.global.response.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<Void>> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(Integer.parseInt(e.getErrorCode().getCode()))
                .body(Response.failure(e.getErrorCode().getCode(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGeneralException(Exception e) {
        return ResponseEntity
                .status(500)
                .body(Response.failure("500", e.getMessage()));
    }
}
