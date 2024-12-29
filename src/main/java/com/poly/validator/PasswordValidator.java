package com.poly.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Custom validator logic
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Nếu password là chuỗi rỗng thì bỏ qua
        if (password == null || password.isEmpty()) {
            return true;
        }

        // Kiểm tra độ dài nếu password không rỗng
        return password.length() >= 6;
    }
}
