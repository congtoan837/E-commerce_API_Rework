package com.poly.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.poly.ex.IPClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoggingService {
    private static final String REQUEST_ID = "request_id";

    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        StringBuilder data = new StringBuilder();
        try {
            data.append("\nLOGGING REQUEST BODY-----------------------------------\n")
                    .append("[REQUEST-ID]: ")
                    .append(requestId)
                    .append("\n")
                    .append("[IPClient]: ")
                    .append(IPClient.getClientIp(httpServletRequest))
                    .append("\n")
                    .append("[SESSION]: ")
                    .append(httpServletRequest.getSession().getId())
                    .append("\n")
                    .append("[BODY REQUEST]: ")
                    .append("\n\n")
                    .append(GsonParserUtils.parseObjectToString(body))
                    .append("\n\n")
                    .append("LOGGING REQUEST BODY-----------------------------------\n");
            log.info(data.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void logResponse(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        StringBuilder data = new StringBuilder();
        try {
            data.append("\nLOGGING RESPONSE-----------------------------------\n")
                    .append("[REQUEST-ID]: ")
                    .append(requestId)
                    .append("\n")
                    .append("[RESPONSE-CODE]: ")
                    .append(httpServletResponse.getStatus())
                    .append("\n")
                    //                .append("[ERROR-MESSAGE:
                    // ").append(httpServletResponse.getOutputStream()).append("\n")
                    .append("LOGGING RESPONSE-----------------------------------\n");
            log.info(data.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
