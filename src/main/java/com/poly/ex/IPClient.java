package com.poly.ex;

import jakarta.servlet.http.HttpServletRequest;

import java.util.StringTokenizer;

public class IPClient {
    public static String getClientIp(HttpServletRequest request){
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }
}
