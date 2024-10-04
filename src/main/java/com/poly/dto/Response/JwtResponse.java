package com.poly.dto.Response;

import lombok.*;

@Data
@Builder
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
}
