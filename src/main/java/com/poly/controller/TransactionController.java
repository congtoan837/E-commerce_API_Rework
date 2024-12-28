package com.poly.controller;

import com.poly.dto.response.ApiResponse;
import com.poly.dto.response.order.OrderResponse;
import com.poly.services.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;

    @GetMapping("/IPN")
    public ApiResponse<?> handleVNPAYCallback(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            fields.put(paramName, paramValue);
        }
        return ApiResponse.builder()
                .result(transactionService.handlePaymentCallback(fields))
                .build();
    }

    @PostMapping("/forceCallback")
    public void forceVNPAYCallback(@RequestBody Map<String, String> request) {
        transactionService.handlePaymentCallback(request);
    }
}
