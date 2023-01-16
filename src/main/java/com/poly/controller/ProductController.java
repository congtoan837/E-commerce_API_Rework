package com.poly.controller;

import com.poly.dto.product.ProductGetDto;
import com.poly.dto.product.ProductPostDto;
import com.poly.entity.*;
import com.poly.ex.ModelMapperConfig;
import com.poly.services.*;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    private ModelMapperConfig mapper;

    @GetMapping("/getAllProduct")
    public ResponseEntity<?> getAllProduct(@RequestParam int page, @RequestParam int size, @RequestParam(defaultValue = "") String search) {
        try {
            Page<Product> products = productService.getAllProduct(search, PageRequest.of(page, size));
            Page<ProductGetDto> result = mapper.mapEntityPageIntoDtoPage(products, ProductGetDto.class);
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get product success!", result.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductPostDto request) {
        try {
            if (StringUtils.isBlank(request.getName()))
                return responseUtils.getResponseEntity("-1", "Product name cant be null!", HttpStatus.BAD_REQUEST);
            if (request.getPrice() < 0)
                return responseUtils.getResponseEntity("-1", "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            if (productService.findByName(request.getName()) != null)
                return responseUtils.getResponseEntity("-1", "Product name is already exists!", HttpStatus.BAD_REQUEST);

            Product product = productService.save(mapper.map(request, Product.class));
            return responseUtils.getResponseEntity(product, "1", "Create product success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody ProductPostDto request) {
        try {
            if (StringUtils.isBlank(request.getName()))
                return responseUtils.getResponseEntity("-1", "Product name cant be null!", HttpStatus.BAD_REQUEST);
            if (request.getPrice() < 0)
                return responseUtils.getResponseEntity("-1", "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            if (productService.findByName(request.getName()) != null)
                return responseUtils.getResponseEntity("-1", "Product name is already exists!", HttpStatus.BAD_REQUEST);

            Product product = productService.getById(request.getId());
            if (product == null) {
                return responseUtils.getResponseEntity("-1", "Product not found!", HttpStatus.BAD_REQUEST);
            }

            Product productUpdate = productService.save(mapper.map(request, Product.class));
            return responseUtils.getResponseEntity(productUpdate, "1", "Update product success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestBody ProductPostDto request) {
        try {
            Product product = productService.getById(request.getId());
            if (product == null) {
                return responseUtils.getResponseEntity("-1", "Product not found!", HttpStatus.BAD_REQUEST);
            }

            productService.deleteById(request.getId());
            return responseUtils.getResponseEntity("1", "Delete product success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }
}
