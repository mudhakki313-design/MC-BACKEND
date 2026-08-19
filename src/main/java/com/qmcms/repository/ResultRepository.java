package com.qmcms.repository;

import com.qmcms.entity.Competition;
import com.qmcms.entity.Juzuu;
import com.qmcms.entity.Participant;
import com.qmcms.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultRepository
        extends JpaRepository<Result, Long> {

    List<Result> findByCompetitionIdAndJuzuuOrderByRankAsc(
            Long competitionId,
            Juzuu juzuu
    );

    Optional<Result> findByParticipant(
            Participant participant
    );

    boolean existsByCompetitionIdAndJuzuu(
            Long competitionId,
            Juzuu juzuu
    );

    void deleteByCompetitionIdAndJuzuu(
            Long competitionId,
            Juzuu juzuu
    );
}