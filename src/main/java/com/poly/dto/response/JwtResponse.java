package com.poly.dto.response;

import lombok.*;

@Data
@Builder
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
}
