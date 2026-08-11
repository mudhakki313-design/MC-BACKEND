package com.qmcms.repository;

import com.qmcms.entity.Judge;
import com.qmcms.entity.Participant;
import com.qmcms.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByParticipant(Participant participant);

    List<Score> findByJudge(Judge judge);

    Optional<Score> findByParticipantAndJudge(
            Participant participant,
            Judge judge
    );

    List<Score> findByParticipantCompetitionId(Long competitionId);

}