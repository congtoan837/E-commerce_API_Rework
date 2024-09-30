package com.poly.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class errorCode {
    private int code;
    private String message;
}
