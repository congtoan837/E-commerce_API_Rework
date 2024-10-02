package com.poly.exception;

import com.poly.dto.Response.ApiResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {
    @ResponseBody
    public static <T> ApiResponse<T> AppResponse(T body) {
        return new ApiResponse<>(1, null, body);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingValidation(AppException exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(-1)
                .message(exception.getErrorCode().getMessage())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }


}
