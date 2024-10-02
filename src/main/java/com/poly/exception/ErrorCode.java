package com.poly.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {
    UNAUTHENTICATED("Unauthenticated"),
    USER_NOT_FOUND("user_not_found"),
    USERNAME_EXITS("username_exits"),
    USERNAME_SHORT("username_too_short"),
    PASSWORD_SHORT("password_too_short"),
    EMAIL_REGEX("email_incorrect"),


;
    private String message;

    public String getMessage() {
        return message;
    }
}
