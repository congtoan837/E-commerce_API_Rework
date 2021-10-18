package com.poly.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.poly.dto.ImageDto;
import com.poly.dto.ProductDetailGetDto;
import com.poly.dto.ProductGetDto;
import com.poly.dto.ProductPostDto;
import com.poly.entity.Image;
import com.poly.entity.Product;
import com.poly.ex.AmazonClient;
import com.poly.services.ProductService;
import com.poly.services.ResponseUtils;
import com.poly.services.ReviewService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Autowired
    private AmazonClient amazonClient;

    private static String convert(String str) {
        //Đổi ký tự có dấu thành không dấu
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");

        str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replaceAll("Đ", "D");
        //Đổi khoảng trắng thành ký tự gạch ngang
        str = str.replaceAll("[\\s]", "-");

        return str.toLowerCase(Locale.ENGLISH);
    }

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

    @GetMapping("/getDetailProduct")
    public ResponseEntity<?> getDetailProduct(Long id) {
        try {
            Product product = productService.getById(id);

            if (product != null) {
                ProductDetailGetDto result = mapper.map(product, ProductDetailGetDto.class);
                return responseUtils.getResponseEntity(result, "-1", "Get product success!", HttpStatus.OK);
            } else {
                return responseUtils.getResponseEntity("-1", "Get product fail!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get product fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@ModelAttribute ProductPostDto request, @ModelAttribute MultipartFile[] files, HttpServletRequest HttpRequest) {
        try {
            if (productService.findByName(request.getName()) != null) {
                return responseUtils.getResponseEntity("-1", "Product name is already exists!", HttpStatus.BAD_REQUEST);
            } else if (request.getName() == null || request.getName().equals("")) {
                return responseUtils.getResponseEntity("-1", "Product name cant be null!", HttpStatus.BAD_REQUEST);
            } else if (request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                return responseUtils.getResponseEntity("-1", "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            } else {
                Product product = mapper.map(request, Product.class);

                Set<Image> images = new HashSet<>();

                Arrays.stream(files).map(file -> {
                    try {
                        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                        if (extension != null && extension.matches("(png|jpg|jpeg|PNG|JPG|JPEG)")) {
                            ImageDto imageDto = amazonClient.uploadFile(file);
                            Image img = new Image();
                            img.setName(imageDto.getName());
                            img.setUrl(imageDto.getUrl());
                            img.setProduct(product);

                            images.add(img);
                            return "Upload success!";
                        } else {
                            return "Upload failer!";
                        }
                    } catch (AmazonS3Exception s3) {
                        return s3.getMessage();
                    }
                }).collect(Collectors.toList());

                product.setImages(images);
                product.setUrl(convert(request.getName()));
                productService.save(product);
                return responseUtils.getResponseEntity("1", "Create product success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", e.getMessage(), HttpStatus.BAD_REQUEST);
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
            } else if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                return responseUtils.getResponseEntity("-1", "Product price cant less than 0!", HttpStatus.BAD_REQUEST);
            } else {
                Long id = product.getId();
                Product getProduct = productService.getById(id);

                if (getProduct != null) {
                    getProduct.setName(product.getName());
                    getProduct.setUrl(product.getUrl().trim());
                    getProduct.setNote(product.getNote());
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
