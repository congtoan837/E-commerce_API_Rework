package com.poly.services;

import org.springframework.stereotype.Service;

import com.poly.dto.request.VariantRequest;
import com.poly.dto.response.product.VariantResponse;
import com.poly.entity.Variant;
import com.poly.mapper.VariantMapper;
import com.poly.repositories.VariantRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantService {
    VariantRepository variantRepository;
    VariantMapper variantMapper;

    public VariantResponse create(VariantRequest request) {
        Variant variant = variantMapper.toVariant(request);

        return variantMapper.toVariantResponse(variant);
    }
}
