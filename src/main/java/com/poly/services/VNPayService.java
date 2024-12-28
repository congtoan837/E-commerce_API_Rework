package com.poly.services;

import com.poly.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class VNPayService {
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

    public boolean verifySignature(Map<String, String> responseParams) {
        String vnp_SecureHash = responseParams.get("vnp_SecureHash");
        if (vnp_SecureHash == null || vnp_SecureHash.isEmpty()) {
            return false;
        }

        Map<String, String> sortedParams = new TreeMap<>(responseParams);
        sortedParams.remove("vnp_SecureHash");

        StringBuilder signData = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (signData.length() > 0) {
                signData.append("&");
            }
            try {
                signData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                signData.append("=");
                signData.append(URLEncoder.encode(entry.getValue() == null ? "" : entry.getValue(), StandardCharsets.UTF_8));
            } catch (Exception e) {
                log.error("Error encoding parameter: {}", entry.getKey());
                e.printStackTrace();
                return false;
            }
        }

        String queryUrl = signData.toString();
        String calculatedHash = hmacSHA512(this.secretKey, queryUrl);

        return calculatedHash.equals(vnp_SecureHash);
    }

    public String createPaymentUrl(Order order) {
        // Các tham số cần thiết cho VNPay
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", this.vnp_TmnCode);
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", String.valueOf(order.getId())); // Mã đơn hàng
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + order.getId());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        params.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        params.put("vnp_ExpireDate", vnp_ExpireDate);

        String bankCode = "VNBANK";
        params.put("vnp_Amount", String.valueOf((int) (order.getTotalAmount() * 100))); // VNPay yêu cầu số tiền * 100
        if (bankCode != null && !bankCode.isEmpty()) {
            params.put("vnp_BankCode", bankCode);
        }
        params.put("vnp_IpAddr", "127.0.0.1");

        // Tạo URL với chữ ký (signature)
        String queryUrl = getPaymentURL(params, true);
        String hashData = getPaymentURL(params, false);

        String vnpSecureHash = hmacSHA512(this.secretKey, hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        return vnp_PayUrl + "?" + queryUrl;
    }

    public String checkTransaction(String txnRef) {
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "querydr");
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_TxnRef", txnRef);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        params.put("vnp_CreateDate", formatter.format(calendar.getTime()));

        String queryUrl = getPaymentURL(params, true);
        String hashData = getPaymentURL(params, false);

        String vnpSecureHash = hmacSHA512(secretKey, hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        return vnp_ApiUrl + "?" + queryUrl;
    }

    public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) {
        return paramsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        (encodeKey ? URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII) : entry.getKey())
                                + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
}

