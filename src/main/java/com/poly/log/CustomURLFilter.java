package com.poly.log;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomURLFilter implements Filter {

    private static final String REQUEST_ID = "request_id";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            // Tạo request ID ngẫu nhiên
            String requestId = UUID.randomUUID().toString();
            servletRequest.setAttribute(REQUEST_ID, requestId);

            // Ghi log thông tin request
            logRequest((HttpServletRequest) servletRequest, requestId);

            // Tiếp tục chuỗi filter
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error("Exception occurred while filtering request", e);
            throw e;
        }
    }

    private void logRequest(HttpServletRequest request, String requestId) {
        if (request != null) {
            StringBuilder data = new StringBuilder();
            data.append("\n")
                    .append("LOGGING REQUEST----------------------------------------")
                    .append("\n")
                    .append("[REQUEST-ID]: ").append(requestId)
                    .append("\n")
                    .append("[USERNAME]: ").append(request.getRemoteUser() != null ? request.getRemoteUser() : "Anonymous")
                    .append("\n")
                    .append("[PATH]: ").append(request.getRequestURI())
                    .append("\n")
                    .append("[QUERIES]: ").append(request.getQueryString() != null ? request.getQueryString() : "No query")
                    .append("\n");

//                    .append("[HEADERS]: ")
//                    .append("\n");
//
//            // Duyệt qua tất cả các header của request
//            Enumeration<String> headerNames = request.getHeaderNames();
//            while (headerNames.hasMoreElements()) {
//                String key = headerNames.nextElement();
//                String value = request.getHeader(key);
//                data.append("---").append(key).append(" : ").append(value).append("\n");
//            }

            data.append("LOGGING REQUEST----------------------------------------\n");
            log.info(data.toString());
        }
    }
}
