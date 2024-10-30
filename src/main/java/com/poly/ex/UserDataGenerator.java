package com.poly.ex;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.poly.entity.Role;
import com.poly.entity.User;

public class UserDataGenerator {
    private static final String[] VIETNAMESE_NAMES = {
        "Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Vũ Văn E",
        "Hoàng Thị F", "Ngô Văn G", "Phạm Văn H", "Đinh Thị I", "Lý Văn J"
    };

    private static final String[] ADDRESSES = {
        "Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng", "Cần Thơ", "Hải Phòng",
        "Nha Trang", "Vũng Tàu", "Huế", "Đà Lạt", "Vinh"
    };

    // Hàm tạo danh sách nhiều User mẫu
    public static Set<User> createSampleUsers() {
        // Tạo roles giả lập
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name("USER").build());

        Set<User> users = new HashSet<>();
        for (int i = 0; i < VIETNAMESE_NAMES.length; i++) {
            String name = VIETNAMESE_NAMES[i];
            String address = ADDRESSES[i];

            // Tạo email và số điện thoại giả lập từ tên và địa chỉ
            String email = createEmailFromName(name);
            String phone = "09" + (i + 1) + "3456789";

            // Tạo đối tượng User từ tên, địa chỉ và thông tin khác
            users.add(User.builder()
                    .name(name) // Tên từ mảng
                    .email(StringUtils.stripAccents(email)) // Email từ hàm tạo email
                    .phone(phone) // Số điện thoại giả lập
                    .address(address) // Địa chỉ từ mảng
                    .image("image" + (i + 1) + ".jpg") // Hình ảnh giả lập
                    .username("userExample " + i) // Tạo username
                    .password("123456") // Mật khẩu giả lập
                    .verifyCode(100000 + i) // Mã xác minh
                    .isDeleted(false) // Trạng thái xoá
                    .roles(roles) // Tạo roles
                    .build());
        }
        return users;
    }

    // Hàm tạo email từ tên
    private static String createEmailFromName(String name) {
        String emailName = name.toLowerCase().replaceAll(" ", "").replaceAll("[đĐ]", "d");
        return emailName + "@example.com";
    }
}
