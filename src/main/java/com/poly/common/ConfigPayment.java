package com.poly.common;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;

import com.poly.dto.request.PaymentRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigPayment {
    @Value("${vnp_PayUrl}")
    protected String vnp_PayUrl;

    @Value("${vnp_ReturnUrl}")
    protected String vnp_ReturnUrl;

    @Value("${vnp_TmnCode}")
    protected String vnp_TmnCode;

    @Value("${vnp_secretKey}")
    protected String secretKey;

    @Value("${vnp_ApiUrl}")
    protected String vnp_ApiUrl;

    public Map<String, String> getVNPayConfig(UUID orderId) {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", "2.1.0");
        vnpParamsMap.put("vnp_Command", "pay");
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef", orderId.toString());
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + orderId);
        vnpParamsMap.put("vnp_OrderType", "other");
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnpParamsMap;
    }

    public void createVnPayPayment(PaymentRequest request) {
        long amount = Integer.parseInt(String.valueOf(request.getAmount())) * 100L;
        String bankCode = "VNBANK";
        Map<String, String> vnpParamsMap = getVNPayConfig(request.getOrderId());
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", "127.0.0.1");
        // build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(this.secretKey, hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = this.vnp_PayUrl + "?" + queryUrl;

        log.info(paymentUrl);
    }
}
