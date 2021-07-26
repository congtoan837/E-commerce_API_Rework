package com.poly.services;

import com.poly.entity.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findByNameLike(String name);

    Product findByName(String name);

    Product findByUrl(String url);

    Page<Product> findAll(Pageable pageable);

    Product save(Product entity);

    Optional<Product> findById(Long aLong);

    void deleteById(Long aLong);

    <S extends Product> long count(Example<S> example);
}
