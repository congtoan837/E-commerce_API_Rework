package com.poly.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.poly.dto.request.VariantRequest;
import com.poly.dto.response.product.VariantResponse;
import com.poly.entity.Variant;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    @Mapping(target = "product", ignore = true)
    Variant toVariant(VariantRequest request);

    VariantResponse toVariantResponse(Variant variant);

    void updateVariantFromVariantRequest(@MappingTarget Variant variant, VariantRequest request);
}
