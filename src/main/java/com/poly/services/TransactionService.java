package com.poly.services;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.poly.entity.Order;
import com.poly.entity.Transaction;
import com.poly.ex.content.TransactionStatus;
import com.poly.exception.AppException;
import com.poly.exception.ErrorCode;
import com.poly.repositories.OrderRepository;
import com.poly.repositories.TransationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionService {
    TransationRepository transationRepository;
    OrderRepository orderRepository;

    VNPayService vnPayService;

    public String initiateTransaction(Order order) {
        Transaction transaction = Transaction.builder()
                .order(order)
                .paymentMethod("VNPAY")
                .amount(order.getTotalAmount())
                .status("PENDING")
                .build();
        transationRepository.save(transaction);
        return vnPayService.createPaymentUrl(order);
    }

    @Transactional
    public boolean handlePaymentCallback(Map<String, String> responseParams) {
        log.info("Payment callback received: {}", responseParams);

        // Kiểm tra chữ ký (signature) từ cổng thanh toán
        boolean isValid = vnPayService.verifySignature(responseParams);
        if (!isValid) return false;

        // Lấy thông tin thanh toán từ database
        UUID orderId = UUID.fromString(responseParams.get("vnp_TxnRef")); // Lấy orderId từ responseParams
        Transaction transaction = transationRepository
                .findByOrderId(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Cập nhật thông tin giao dịch
        String transactionId = responseParams.get("vnp_TransactionNo");
        String responseCode = responseParams.get("vnp_ResponseCode");

        TransactionStatus status = TransactionStatus.fromCode(responseCode);

        transaction.setTransactionId(transactionId);
        transaction.setResponseCode(responseCode);
        transaction.setStatus(status.name());
        transationRepository.save(transaction);

        // Cập nhật trạng thái đơn hàng nếu thanh toán thành công
        if (Objects.equals(TransactionStatus.SUCCESS, status)) {
            Order order = transaction.getOrder();
            order.setStatus("PROCESSING"); // Hoặc trạng thái phù hợp
            orderRepository.save(order); // Lưu cập nhật đơn hàng
        }

        return Objects.equals(TransactionStatus.SUCCESS, status);
    }
}
