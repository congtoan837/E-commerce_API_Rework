package com.poly.dto.Response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
}
