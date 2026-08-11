package com.qmcms.repository;

import com.qmcms.entity.Juzuu;
import com.qmcms.entity.Madrasa;
import com.qmcms.entity.Participant;
import com.qmcms.entity.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByMadrasa(Madrasa madrasa);

    List<Participant> findByStatus(ParticipantStatus status);
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