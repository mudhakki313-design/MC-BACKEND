package com.qmcms.repository;

import com.qmcms.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JudgeRepository extends JpaRepository<Judge, Long> {

    boolean existsByJudgeNumber(String judgeNumber);

    boolean existsByEmail(String email);
    Optional<Judge> findByUserUsername(String username);
}