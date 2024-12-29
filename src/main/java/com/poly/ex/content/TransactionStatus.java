package com.poly.ex.content;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatus {
    SUCCESS("00"), // "Giao dịch thành công"
    SUSPECTED("07"), // "Giao dịch bị nghi ngờ (liên quan tới lừa đảo)"
    NOT_REGISTERED("09"), // "Thẻ/Tài khoản chưa đăng ký InternetBanking"
    INVALID_AUTH("10"), // "Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"
    EXPIRED("11"), // "Đã hết hạn chờ thanh toán"
    BLOCKED("12"), // "Thẻ/Tài khoản bị khóa"
    WRONG_OTP("13"), // "Nhập sai mật khẩu xác thực giao dịch (OTP)"
    CANCELLED("24"), // "Khách hàng hủy giao dịch"
    INSUFFICIENT_FUNDS("51"), // "Tài khoản không đủ số dư"
    EXCEEDED_LIMIT("65"), // "Vượt quá hạn mức giao dịch trong ngày"
    MAINTENANCE("75"), // "Ngân hàng thanh toán đang bảo trì"
    WRONG_PAYMENT_PASSWORD("79"), // "Nhập sai mật khẩu thanh toán"
    OTHER_ERROR("99"); // "Các lỗi khác"

    private final String code;

    public static TransactionStatus fromCode(String code) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return OTHER_ERROR; // Trả về OTHER_ERROR nếu không tìm thấy mã
    }
}
