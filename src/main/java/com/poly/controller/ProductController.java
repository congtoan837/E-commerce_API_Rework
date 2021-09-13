package com.poly.controller;

import com.poly.dto.ProductGetDto;
import com.poly.entity.Product;
import com.poly.services.ProductService;
import com.poly.services.ResponseUtils;
import com.poly.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private ModelMapper mapper;

    @GetMapping("/getAllProduct")
    public ResponseEntity<?> getAllProduct(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy,
                                           @RequestParam String sortType, @RequestParam(defaultValue = "") String search) {
        try {
            String S = sortType.trim().toLowerCase();
            Page<Product> products = productService.getAllProduct(search, PageRequest.of(page, size, Sort.by(S.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
            Page<Object> result = products.map(product -> mapper.map(product, ProductGetDto.class));
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get product success!", products.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get product fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            if (productService.findByName(product.getName()) != null) {
                return responseUtils.getResponseEntity("-1", "Product name is already exists!", HttpStatus.BAD_REQUEST);
            } else if (productService.findByUrl(product.getUrl()) != null) {
                return responseUtils.getResponseEntity("-1", "Product url is already exists!", HttpStatus.BAD_REQUEST);
            } else if (product.getUrl() == null || product.getUrl().equals("")) {
                return responseUtils.getResponseEntity("-1", "Product url cant be null!", HttpStatus.BAD_REQUEST);
            } else if (product.getPrice() < 0) {
                return responseUtils.getResponseEntity("-1", "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            } else {
                productService.save(product);
                return responseUtils.getResponseEntity("1", "Create product success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create product fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        try {
            if (productService.findByName(product.getName()) != null) {
                return responseUtils.getResponseEntity("-1", "Product name is already exists!", HttpStatus.BAD_REQUEST);
            } else if (productService.findByUrl(product.getUrl()) != null) {
                return responseUtils.getResponseEntity("-1", "Product url is already exists!", HttpStatus.BAD_REQUEST);
            } else if (product.getUrl() == null || product.getUrl().equals("")) {
                return responseUtils.getResponseEntity("-1", "Product url cant be null!", HttpStatus.BAD_REQUEST);
            } else if (product.getPrice() < 0) {
                return responseUtils.getResponseEntity("-1", "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            } else {
                Long id = product.getId();
                Product getProduct = productService.getById(id);

                if (getProduct != null) {
                    getProduct.setName(product.getName());
                    getProduct.setUrl(product.getUrl().trim());
                    getProduct.setShortDescription(product.getShortDescription());
                    getProduct.setPrice(product.getPrice());

                    if (product.getCategories().size() > 0) {
                        getProduct.setCategories(product.getCategories());
                    }
                    if (product.getImages().size() > 0) {
                        getProduct.setImages(product.getImages());
                    }

                    productService.save(getProduct);
                    return responseUtils.getResponseEntity("1", "Update product success!", HttpStatus.OK);
                } else {
                    return responseUtils.getResponseEntity("-1", "Product " + id + " not found!", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Update product fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            Product getProduct = productService.getById(id);
            if (getProduct == null) {
                return responseUtils.getResponseEntity("-1", "Product " + id + " not found!", HttpStatus.BAD_REQUEST);
            } else {
                productService.deleteById(id);
                return responseUtils.getResponseEntity("1", "Delete product success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }
}
