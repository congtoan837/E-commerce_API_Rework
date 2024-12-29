package com.poly.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private UUID id;
    private int rating;
    private String title;
    private String comment;

    @NotBlank
    private UUID productId;

    @NotBlank
    private UUID userId;
}
