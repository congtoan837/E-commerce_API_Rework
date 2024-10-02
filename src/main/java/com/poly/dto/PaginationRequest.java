package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {
    private int page = 1;
    private int size = 10;
    private String sortBy = "createTime";
    private int sortDirection = -1;
}
