package com.poly.services;

import com.poly.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ImageService {

    Page<Image> findAll(Pageable pageable);

    Image save(Image entity);

    Optional<Image> findById(Long aLong);

    long count();

    void deleteById(Long aLong);
}
