package com.qmcms.repository;

import com.qmcms.entity.Juzuu;
import com.qmcms.entity.Madrasa;
import com.qmcms.entity.Participant;
import com.qmcms.entity.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository
        extends JpaRepository<Participant, Long> {


    // =====================================================
    // MADRASA
    // =====================================================

    List<Participant> findByMadrasa(Madrasa madrasa);


    Optional<Participant> findByIdAndMadrasa(
            Long id,
            Madrasa madrasa
    );


    // =====================================================
    // STATUS
    // =====================================================

    List<Participant> findByStatus(
            ParticipantStatus status
    );


    // =====================================================
    // COMPETITION + JUZUU
    // =====================================================

    List<Participant> findByCompetitionIdAndJuzuu(
            Long competitionId,
            Juzuu juzuu
    );


    List<Participant> findByCompetitionIdAndJuzuuAndStatus(
            Long competitionId,
            Juzuu juzuu,
            ParticipantStatus status
    );

}