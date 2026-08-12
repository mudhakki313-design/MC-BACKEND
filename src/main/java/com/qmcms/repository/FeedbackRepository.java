package com.qmcms.repository;

import com.qmcms.entity.Feedback;
import com.qmcms.entity.Madrasa;
import com.qmcms.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByCompetitionAndMadrasa(
            Competition competition,
            Madrasa madrasa
    );

    List<Feedback> findByCompetitionId(Long competitionId);
}