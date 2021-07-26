package com.poly.controller;

import com.poly.entity.Image;
import com.poly.entity.Product;
import com.poly.entity.User;
import com.poly.services.ProductService;
import com.poly.services.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ResponseUtils responseUtils;

    @GetMapping("/getAllProduct")
    public ResponseEntity<?> getAllProduct(@RequestParam int page) {
        try {
            Page<Product> products = productService.findAll(PageRequest.of(page, 5, Sort.by("name").ascending()));
            return responseUtils.getResponseEntity(products, "1", "Get product success!", HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get product fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createUser(@RequestBody Product product, Authentication authentication) {
        try {
            if (productService.findByName(product.getName()) != null) {
                return responseUtils.getResponseEntity("-1", "Product name is already exists!", HttpStatus.BAD_REQUEST);
            } else if (productService.findByUrl(product.getUrl()) != null) {
                return responseUtils.getResponseEntity("-1", "Product url is already exists!", HttpStatus.BAD_REQUEST);
            } else if (product.getUrl() == null || product.getUrl() == "") {
                return responseUtils.getResponseEntity("-1", "Product url cant be null!", HttpStatus.BAD_REQUEST);
            } else if (product.getPrice() < 0) {
                return responseUtils.getResponseEntity("-1", "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            } else {
                Image image = new Image();
                image.setName("test1");
                image.setUrl("test1");

                List<Image> images = new ArrayList<Image>();
                images.add(image);

                product.setImages(images);
                productService.save(product);
                return responseUtils.getResponseEntity(product, "1", "Create product success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create product fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/create")
    public ResponseEntity<?> create() {
        Product product = new Product();
        Image image = new Image();
        image.setName("a");
        image.setUrl("a");

        List<Image> images = new ArrayList<Image>();
        images.add(image);

        product.setName("Toan1");
        product.setImages(images);

        productService.save(product);

        return responseUtils.getResponseEntity("-1", "Create product success!", HttpStatus.BAD_REQUEST);
    }
}
