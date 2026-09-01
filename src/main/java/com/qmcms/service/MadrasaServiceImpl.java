package com.qmcms.service;

import com.qmcms.dto.request.MadrasaRequest;
import com.qmcms.dto.response.MadrasaResponse;
import com.qmcms.entity.*;
import com.qmcms.repository.MadrasaRepository;
import com.qmcms.repository.UserRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MadrasaServiceImpl
        implements MadrasaService {


    private final MadrasaRepository madrasaRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    // =====================================================
    // CREATE MADRASA
    // =====================================================

    @Override
    @Transactional
    public MadrasaResponse createMadrasa(
            MadrasaRequest request
    ) {

        if (
                madrasaRepository
                        .existsByRegistrationNumber(
                                request.getRegistrationNumber()
                        )
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registration number already exists."
            );

        }


        if (
                madrasaRepository
                        .existsByEmail(
                                request.getEmail()
                        )
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists."
            );

        }


        if (
                userRepository
                        .existsByUsername(
                                request.getRegistrationNumber()
                        )
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username already exists."
            );

        }


        User user =
                User.builder()

                        .fullName(
                                request.getContactPerson()
                        )

                        .username(
                                request.getRegistrationNumber()
                        )

                        .email(
                                request.getEmail()
                        )

                        .password(
                                passwordEncoder.encode(
                                        request.getRegistrationNumber()
                                                + "@2026"
                                )
                        )

                        .role(
                                Role.ROLE_MADRASA
                        )

                        .status(
                                UserStatus.ACTIVE
                        )

                        .build();


        userRepository.save(user);


        Madrasa madrasa =
                Madrasa.builder()

                        .name(
                                request.getName()
                        )

                        .registrationNumber(
                                request.getRegistrationNumber()
                        )

                        .region(
                                request.getRegion()
                        )

                        .district(
                                request.getDistrict()
                        )

                        .contactPerson(
                                request.getContactPerson()
                        )

                        .phone(
                                request.getPhone()
                        )

                        .email(
                                request.getEmail()
                        )

                        .address(
                                request.getAddress()
                        )

                        .status(
                                request.getStatus()
                        )

                        .user(
                                user
                        )

                        .build();


        madrasaRepository.save(
                madrasa
        );


        return mapToResponse(
                madrasa
        );

    }


    // =====================================================
    // GET ALL MADRASAS
    // ASSOCIATION / CHIEF JUDGE
    // =====================================================

    @Override
    public List<MadrasaResponse> getAllMadrasas() {

        return madrasaRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }


    // =====================================================
    // GET ONE MADRASA
    // =====================================================

    @Override
    public MadrasaResponse getMadrasaById(
            Long id
    ) {

        return mapToResponse(
                findMadrasa(id)
        );

    }


    // =====================================================
    // GET LOGGED-IN MADRASA
    // =====================================================

    @Override
    public MadrasaResponse getMyMadrasa() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (
                authentication == null ||
                        !authentication.isAuthenticated()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not authenticated."
            );

        }


        String username =
                authentication.getName();


        if (
                username == null ||
                        username.isBlank()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated username could not be determined."
            );

        }


        Madrasa madrasa =
                madrasaRepository
                        .findByUser_Username(
                                username
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Madrasa associated with this account was not found."
                                )
                        );


        return mapToResponse(
                madrasa
        );

    }


    // =====================================================
    // UPDATE MADRASA
    // =====================================================

    @Override
    @Transactional
    public MadrasaResponse updateMadrasa(
            Long id,
            MadrasaRequest request
    ) {

        Madrasa madrasa =
                findMadrasa(id);


        if (
                madrasaRepository
                        .existsByEmailAndIdNot(
                                request.getEmail(),
                                id
                        )
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists."
            );

        }


        madrasa.setName(
                request.getName()
        );

        madrasa.setRegion(
                request.getRegion()
        );

        madrasa.setDistrict(
                request.getDistrict()
        );

        madrasa.setContactPerson(
                request.getContactPerson()
        );

        madrasa.setPhone(
                request.getPhone()
        );

        madrasa.setEmail(
                request.getEmail()
        );

        madrasa.setAddress(
                request.getAddress()
        );

        madrasa.setStatus(
                request.getStatus()
        );


        if (
                madrasa.getUser() != null
        ) {

            madrasa.getUser().setFullName(
                    request.getContactPerson()
            );

            madrasa.getUser().setEmail(
                    request.getEmail()
            );

        }


        return mapToResponse(
                madrasaRepository.save(
                        madrasa
                )
        );

    }


    // =====================================================
    // DEACTIVATE
    // =====================================================

    @Override
    @Transactional
    public void deactivateMadrasa(
            Long id
    ) {

        Madrasa madrasa =
                findMadrasa(id);


        madrasa.setStatus(
                MadrasaStatus.INACTIVE
        );


        if (
                madrasa.getUser() != null
        ) {

            madrasa.getUser().setStatus(
                    UserStatus.INACTIVE
            );

        }


        madrasaRepository.save(
                madrasa
        );

    }


    // =====================================================
    // FIND MADRASA
    // =====================================================

    private Madrasa findMadrasa(
            Long id
    ) {

        return madrasaRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Madrasa not found."
                        )
                );

    }


    // =====================================================
    // MAP RESPONSE
    // =====================================================

    private MadrasaResponse mapToResponse(
            Madrasa madrasa
    ) {

        return MadrasaResponse.builder()

                .id(
                        madrasa.getId()
                )

                .name(
                        madrasa.getName()
                )

                .registrationNumber(
                        madrasa.getRegistrationNumber()
                )

                .region(
                        madrasa.getRegion()
                )

                .district(
                        madrasa.getDistrict()
                )

                .contactPerson(
                        madrasa.getContactPerson()
                )

                .phone(
                        madrasa.getPhone()
                )

                .email(
                        madrasa.getEmail()
                )

                .address(
                        madrasa.getAddress()
                )

                .status(
                        madrasa.getStatus()
                )

                .username(
                        madrasa.getUser().getUsername()
                )

                .createdAt(
                        madrasa.getCreatedAt()
                )

                .build();

    }

}