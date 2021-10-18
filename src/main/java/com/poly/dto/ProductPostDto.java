package com.poly.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProductPostDto {

    private Long id;

    private String name;

    private String note;

    private BigDecimal price;

    private Set<CategoryDto> categories;
}
