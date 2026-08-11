package com.qmcms.service;

import com.qmcms.dto.request.MadrasaRequest;
import com.qmcms.dto.response.MadrasaResponse;
import com.qmcms.entity.*;
import com.qmcms.repository.MadrasaRepository;
import com.qmcms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MadrasaServiceImpl implements MadrasaService {

    private final MadrasaRepository madrasaRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MadrasaResponse createMadrasa(MadrasaRequest request) {

        if (madrasaRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registration number already exists."
            );
        }

        if (madrasaRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists."
            );
        }

        if (userRepository.existsByUsername(request.getRegistrationNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username already exists."
            );
        }

        User user = User.builder()
                .fullName(request.getContactPerson())
                .username(request.getRegistrationNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getRegistrationNumber() + "@2026"))
                .role(Role.ROLE_MADRASA)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        Madrasa madrasa = Madrasa.builder()
                .name(request.getName())
                .registrationNumber(request.getRegistrationNumber())
                .region(request.getRegion())
                .district(request.getDistrict())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .status(request.getStatus())
                .user(user)
                .build();

        madrasaRepository.save(madrasa);

        return mapToResponse(madrasa);

    }

    @Override
    public List<MadrasaResponse> getAllMadrasas() {

        return madrasaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public MadrasaResponse getMadrasaById(Long id) {

        return mapToResponse(findMadrasa(id));

    }

    @Override
    @Transactional
    public MadrasaResponse updateMadrasa(Long id,
                                         MadrasaRequest request) {

        Madrasa madrasa = findMadrasa(id);

        madrasa.setName(request.getName());
        madrasa.setRegion(request.getRegion());
        madrasa.setDistrict(request.getDistrict());
        madrasa.setContactPerson(request.getContactPerson());
        madrasa.setPhone(request.getPhone());
        madrasa.setEmail(request.getEmail());
        madrasa.setAddress(request.getAddress());
        madrasa.setStatus(request.getStatus());

        madrasa.getUser().setFullName(request.getContactPerson());
        madrasa.getUser().setEmail(request.getEmail());

        if (madrasaRepository.existsByEmailAndIdNot(request.getEmail(), id)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists."
            );

        }

        return mapToResponse(madrasaRepository.save(madrasa));

    }

    @Override
    @Transactional
    public void deactivateMadrasa(Long id) {

        Madrasa madrasa = findMadrasa(id);

        madrasa.setStatus(MadrasaStatus.INACTIVE);
        madrasa.getUser().setStatus(UserStatus.INACTIVE);

        madrasaRepository.save(madrasa);

    }

    // ==========================
    // Helper Methods
    // ==========================

    private Madrasa findMadrasa(Long id) {

        return madrasaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Madrasa not found."
                ));

    }

    private MadrasaResponse mapToResponse(Madrasa madrasa) {

        return MadrasaResponse.builder()
                .id(madrasa.getId())
                .name(madrasa.getName())
                .registrationNumber(madrasa.getRegistrationNumber())
                .region(madrasa.getRegion())
                .district(madrasa.getDistrict())
                .contactPerson(madrasa.getContactPerson())
                .phone(madrasa.getPhone())
                .email(madrasa.getEmail())
                .address(madrasa.getAddress())
                .status(madrasa.getStatus())
                .username(madrasa.getUser().getUsername())
                .createdAt(madrasa.getCreatedAt())
                .build();

    }

}