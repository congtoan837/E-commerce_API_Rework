package com.poly.controller;

import com.poly.dto.product.ProductGetDto;
import com.poly.dto.product.ProductPostDto;
import com.poly.entity.*;
import com.poly.ex.ModelMapperConfig;
import com.poly.exception.ResponseUtils;
import com.poly.services.*;

import org.apache.commons.lang3.StringUtils;
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
    public ResponseEntity<?> getAllProduct(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String search) {
        try {
            Page<Product> products = productService.getAllProduct(search, PageRequest.of(page, size));
            Page<ProductGetDto> result = mapper.mapEntityPageIntoDtoPage(products, ProductGetDto.class);
            return new ResponseEntity<>(result.getContent(), "Get product success!", result.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductPostDto request) {
        try {
            if (StringUtils.isBlank(request.getName()))
                return new ResponseEntity<>(null, "Product name cant be null!", HttpStatus.BAD_REQUEST);
            if (request.getPrice() < 0)
                return new ResponseEntity<>(null, "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            if (productService.findByName(request.getName()) != null)
                return new ResponseEntity<>(null, "Product name is already exists!", HttpStatus.BAD_REQUEST);

            Product product = productService.save(mapper.map(request, Product.class));
            return new ResponseEntity<>(product, "Create product success!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody ProductPostDto request) {
        try {
            if (StringUtils.isBlank(request.getName()))
                return new ResponseEntity<>(null, "Product name cant be null!", HttpStatus.BAD_REQUEST);
            if (request.getPrice() < 0)
                return new ResponseEntity<>(null, "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            if (productService.findByName(request.getName()) != null)
                return new ResponseEntity<>(null, "Product name is already exists!", HttpStatus.BAD_REQUEST);

            Product product = productService.getById(request.getId());
            if (product == null) {
                return new ResponseEntity<>(null, "Product not found!", HttpStatus.BAD_REQUEST);
            }

            Product productUpdate = productService.save(mapper.map(request, Product.class));
            return new ResponseEntity<>(productUpdate, "Update product success!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestBody ProductPostDto request) {
        try {
            Product product = productService.getById(request.getId());
            if (product == null) {
                return new ResponseEntity<>(null, "Product not found!", HttpStatus.BAD_REQUEST);
            }

            productService.deleteById(request.getId());
            return new ResponseEntity<>(null, "Delete product success!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, "Error processing!", HttpStatus.BAD_REQUEST);
        }
    }
}
