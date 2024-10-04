package com.poly.Config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig extends ModelMapper {
    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }

    // Phương thức chung để map từ danh sách source sang target class
    public <T, U> List<U> mapList(List<T> sourceList, Class<U> targetClass) {
        return sourceList.stream()
                .map(element -> modelMapper().map(element, targetClass))
                .collect(Collectors.toList());
    }
}
