package com.poly.dto.Request;

import lombok.Data;

@Data
public class LoginRequest {
	private String username;
	private String password;
}