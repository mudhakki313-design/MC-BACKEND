package com.qmcms.repository;

import com.qmcms.entity.Madrasa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MadrasaRepository extends JpaRepository<Madrasa, Long> {

    Optional<Madrasa> findByRegistrationNumber(
            String registrationNumber
    );

    boolean existsByRegistrationNumber(
            String registrationNumber
    );

    boolean existsByEmail(
            String email
    );

    boolean existsByEmailAndIdNot(
            String email,
            Long id
    );

    // =====================================================
    // FIND MADRASA USING USER USERNAME
    // =====================================================

    Optional<Madrasa> findByUser_Username(
            String username
    );
}