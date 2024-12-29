package com.poly.log;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoggingService {
    private static final String REQUEST_ID = "request_id";
    private static Gson gson = new Gson();

    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        StringBuilder data = new StringBuilder();
        try {
            data.append("\n")
                    .append("LOGGING REQUEST BODY-----------------------------------")
                    .append("\n")
                    .append("[REQUEST-ID]: ").append(requestId)
                    .append("\n")
                    .append("[SESSION]: ").append(httpServletRequest.getSession().getId())
                    .append("\n")
                    .append("[BODY]: ").append(gson.toJson(body))
                    .append("\n")
                    .append("LOGGING REQUEST BODY-----------------------------------").append("\n");
            log.info(data.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void logResponse(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        StringBuilder data = new StringBuilder();
        try {
            data.append("\n")
                    .append("LOGGING RESPONSE---------------------------------------")
                    .append("\n")
                    .append("[REQUEST-ID]: ").append(requestId)
                    .append("\n")
                    .append("[RESPONSE-CODE]: ").append(httpServletResponse.getStatus())
                    .append("\n")
                    .append("LOGGING RESPONSE---------------------------------------").append("\n");
            log.info(data.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
