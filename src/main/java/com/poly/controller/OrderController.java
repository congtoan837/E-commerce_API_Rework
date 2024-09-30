package com.poly.controller;

import com.poly.entity.*;
import com.poly.exception.ResponseUtils;
import com.poly.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    ResponseUtils responseUtils;

    @GetMapping("/getAllOrder")
    public ResponseEntity<?> getAllOrder(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, Authentication authentication) {
        try {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                Page<Order> orders = orderService.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
                return new ResponseEntity<>(orders, "Get order success!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, "You not have permission to access!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, "Get order fail!", HttpStatus.BAD_REQUEST);
        }
    }
}
