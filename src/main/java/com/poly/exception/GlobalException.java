package com.poly.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    @ResponseBody
    public <T> ResponseEntity<ResponseUtils<T>> wrapResponse(T body) {
        // Tạo một response với code và message mặc định
        ResponseUtils<T> response = new ResponseUtils<>(1, "success", body);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}