package com.poly.exception;

import lombok.Builder;
import lombok.Data;
import org.jboss.logging.Logger;

@Data
@Builder
public class ResponseUtils<T> {
    public int code = 1;
    public String message;
    public T result;
}
