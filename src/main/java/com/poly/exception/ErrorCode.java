package com.poly.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(-9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(-1, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(-1, "Access denied", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(-1, "user not found", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(-1, "Username exists", HttpStatus.BAD_REQUEST),
    USERNAME_SHORT(-1, "Username too short", HttpStatus.BAD_REQUEST),
    PASSWORD_SHORT(-1, "Password too short", HttpStatus.BAD_REQUEST),
    EMAIL_REGEX(-1, "Email incorrect", HttpStatus.BAD_REQUEST),
    ID_NOT_FOUND(-1, "Id not found", HttpStatus.BAD_REQUEST),

;
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
