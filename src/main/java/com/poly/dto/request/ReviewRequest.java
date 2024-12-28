package com.poly.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
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
