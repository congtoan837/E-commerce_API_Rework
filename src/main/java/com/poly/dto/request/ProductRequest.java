package com.poly.dto.request;

import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private UUID id;

    private String name;

    private String note;

    private int price;

    private Set<String> categories;

    private String coverImage;

    private Set<String> images;

    private Set<VariantRequest> variants;
}
