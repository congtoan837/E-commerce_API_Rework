package com.poly.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private final String token;
    private String type = "Bearer";
    private final UUID userId;
    private final String name;
    private final List<String> roles;
}
