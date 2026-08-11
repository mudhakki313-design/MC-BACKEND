package com.qmcms.service;

import com.qmcms.dto.request.LoginRequest;
import com.qmcms.dto.response.LoginResponse;
import com.qmcms.entity.User;
import com.qmcms.repository.UserRepository;
import com.qmcms.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getUsername(),

                        request.getPassword()

                )

        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        return LoginResponse.builder()

                .token(jwtService.generateToken(
                        new org.springframework.security.core.userdetails.User(

                                user.getUsername(),

                                user.getPassword(),

                                java.util.List.of(
                                        new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                                user.getRole().name()
                                        )
                                )

                        )
                ))

                .username(user.getUsername())

                .fullName(user.getFullName())

                .role(user.getRole().name())

                .build();

    }

}