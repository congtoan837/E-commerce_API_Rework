package com.poly.Config;

import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.ex.ERole;
import com.poly.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class ApplicationInitConfig {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.existsByUsername("admin")) return;

            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(Collections.singleton(Role.builder().name(ERole.ADMIN.name()).build()))
                    .build();
            userRepository.save(user);
        };
    }
}
