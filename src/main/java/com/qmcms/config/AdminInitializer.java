package com.qmcms.config;

import com.qmcms.entity.Role;
import com.qmcms.entity.User;
import com.qmcms.entity.UserStatus;
import com.qmcms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = User.builder()
                .fullName("System Administrator")
                .username("admin")
                .email("admin@qmcms.com")
                .password(passwordEncoder.encode("Admin@#123"))
                .role(Role.ROLE_ASSOCIATION)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(admin);

        System.out.println("=================================");
        System.out.println("Default Admin Created");
        System.out.println("Username : admin");
        System.out.println("Password : Admin@#123");
        System.out.println("=================================");

    }

}