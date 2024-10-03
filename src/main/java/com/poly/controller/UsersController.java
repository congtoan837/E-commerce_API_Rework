package com.poly.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UsersController {
    @RequestMapping("/product")
    public class Product {
        @GetMapping("/getProduct")
        public String getProduct() {
            return null;
        }
    }
}
