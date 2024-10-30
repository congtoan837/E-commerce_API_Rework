package com.poly.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.ex.ERole;
import com.poly.repositories.RoleRepository;
import com.poly.repositories.UserRepository;

@Configuration
public class ApplicationInitConfig {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            //            Set<User> userSet = UserDataGenerator.createSampleUsers();
            //            userRepository.saveAll(userSet);

            //            PaymentRequest request = new PaymentRequest();
            //            request.setAmount(10000);
            //            request.setOrderId(UUID.randomUUID());
            //
            //            ConfigPayment configPayment = new ConfigPayment();
            //            configPayment.createVnPayPayment(request);

            if (userRepository.existsByUsernameAndIsDeletedFalse("admin")) return;
            for (ERole eRole : ERole.values()) {
                Role role = Role.builder().name(eRole.name()).build();
                roleRepository.save(role);
            }

            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(Collections.singleton(
                            Role.builder().name(ERole.ADMIN.name()).build()))
                    .build();
            userRepository.save(user);
        };
    }
}
