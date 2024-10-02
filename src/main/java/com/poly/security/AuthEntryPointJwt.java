package com.poly.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		String errorMessage;

		// Kiểm tra lý do xác thực không thành công
		if (authException.getMessage() != null) {
			if (authException.getMessage().contains("expired")) {
				errorMessage = "Unauthorized access. Token has expired.";
			} else if (authException.getMessage().contains("invalid") || authException.getMessage().contains("cannot be authenticated")) {
				errorMessage = "Unauthorized access. Invalid token.";
			} else {
				errorMessage = "Unauthorized access. Please provide a valid token.";
			}
		} else {
			errorMessage = "Unauthorized access. Please provide a valid token.";
		}

		response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
	}
}
