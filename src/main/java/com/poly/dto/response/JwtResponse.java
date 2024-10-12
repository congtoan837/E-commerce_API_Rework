package com.poly.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
