package com.poly.entity;

import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
public class JwtResponse {
    private final  String token;
    private String type = "Bearer";
    private final  Long userId;
    private final  String username;
    private final  List<String> roles;
}
