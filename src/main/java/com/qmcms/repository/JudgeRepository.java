package com.qmcms.repository;

import com.qmcms.entity.Judge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgeRepository extends JpaRepository<Judge, Long> {

    boolean existsByJudgeNumber(String judgeNumber);

    boolean existsByEmail(String email);

}