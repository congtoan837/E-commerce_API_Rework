package com.poly.controller;

import com.poly.dto.OrderGetDto;
import com.poly.dto.OrderPostDto;
import com.poly.entity.Order;
import com.poly.services.AuthService;
import com.poly.services.OrderService;
import com.poly.services.ResponseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    ModelMapper mapper;

    @GetMapping("/getAllOrder")
    public ResponseEntity<?> getAllOrder(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy,
                                         @RequestParam String sortType, @RequestParam(defaultValue = "") String search) {
        try {
            String S = sortType.trim().toLowerCase();
            Page<Order> orders = orderService.getAllOrder(search, PageRequest.of(page, size, Sort.by(S.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
            Page<Object> result = orders.map(product -> mapper.map(product, OrderGetDto.class));
            return responseUtils.getResponseEntity(result.getContent(), "1", "Get order success!", orders.getTotalElements(), HttpStatus.OK);
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Get order fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderPostDto request) {
        try {
            if (!request.getOrderPhone().matches("[0-9]+") || request.getOrderPhone().length() != 10) {
                return responseUtils.getResponseEntity("-1", "Number phone invalid!", HttpStatus.BAD_REQUEST);
            } else if (request.getOrderAddress() == null || request.getOrderAddress().equals("")) {
                return responseUtils.getResponseEntity("-1", "Address invalid!", HttpStatus.BAD_REQUEST);
            } else if (request.getQuantity() <= 0) {
                return responseUtils.getResponseEntity("-1", "Quantity invalid!", HttpStatus.BAD_REQUEST);
            } else {
                Order order = mapper.map(request, Order.class);
                order.setStatus((byte) 0);
                orderService.save(order);
                return responseUtils.getResponseEntity("1", "Create order success!", HttpStatus.OK);
            }
        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Create order fail!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<?> updateOrder(@RequestBody OrderPostDto request) {
        try {
            if (!request.getOrderPhone().matches("[0-9]+") || request.getOrderPhone().length() != 10) {
                return responseUtils.getResponseEntity("-1", "Number phone invalid!", HttpStatus.BAD_REQUEST);
            } else if (request.getOrderAddress() == null || request.getOrderAddress().equals("")) {
                return responseUtils.getResponseEntity("-1", "Address invalid!", HttpStatus.BAD_REQUEST);
            } else if (request.getQuantity() <= 0) {
                return responseUtils.getResponseEntity("-1", "Quantity invalid!", HttpStatus.BAD_REQUEST);
            } else {
                Long orderId = request.getId();
                Order order = orderService.getById(orderId);

                order.setOrderPhone(request.getOrderPhone());
                order.setOrderAddress(request.getOrderAddress());
                order.setQuantity(request.getQuantity());
                order.setNote(request.getNote());

                orderService.save(order);
            }
        } catch (Exception e) {

        }
        return responseUtils.getResponseEntity("-1", "Get order fail!", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            Order order = orderService.getById(id);

            if (order == null) {
                return responseUtils.getResponseEntity("-1", "Order not found!", HttpStatus.BAD_REQUEST);
            } else {
                orderService.deleteById(id);
                return responseUtils.getResponseEntity("1", "Delete order success!", HttpStatus.OK);
            }

        } catch (Exception e) {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/orderProcessing")
    public ResponseEntity<?> orderProcessing(@RequestParam Long orderId) {
        Order order = orderService.getById(orderId);
        if (order.getStatus() == 0) {
            order.setStatus((byte) 1);
            return responseUtils.getResponseEntity("1", "Order status updated!", HttpStatus.OK);
        } else if (order.getStatus() == 1) {
            order.setStatus((byte) 2);
            return responseUtils.getResponseEntity("1", "Order status updated!", HttpStatus.OK);
        } else {
            return responseUtils.getResponseEntity("-1", "Server error!", HttpStatus.BAD_REQUEST);
        }
    }
}
