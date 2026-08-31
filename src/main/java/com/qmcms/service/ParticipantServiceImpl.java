package com.qmcms.service;

import com.qmcms.dto.request.ParticipantRequest;
import com.qmcms.dto.response.ParticipantResponse;
import com.qmcms.entity.Competition;
import com.qmcms.entity.Madrasa;
import com.qmcms.entity.Participant;
import com.qmcms.repository.CompetitionRepository;
import com.qmcms.repository.MadrasaRepository;
import com.qmcms.repository.ParticipantRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;
    private final CompetitionRepository competitionRepository;
    private final MadrasaRepository madrasaRepository;


    // =====================================================
    // ASSOCIATION
    // CREATE PARTICIPANT
    // =====================================================

    @Override
    @Transactional
    public ParticipantResponse createParticipant(
            ParticipantRequest request
    ) {

        Competition competition =
                findCompetition(request.getCompetitionId());

        Madrasa madrasa =
                findMadrasa(request.getMadrasaId());

        Participant participant =
                Participant.builder()
                        .fullName(request.getFullName())
                        .gender(request.getGender())
                        .age(request.getAge())
                        .juzuu(request.getJuzuu())
                        .competition(competition)
                        .madrasa(madrasa)
                        .build();

        participantRepository.save(participant);

        return mapToResponse(participant);
    }


    // =====================================================
    // ASSOCIATION / CHIEF JUDGE
    // GET ALL
    // =====================================================

    @Override
    public List<ParticipantResponse> getAllParticipants() {

        return participantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // ASSOCIATION / CHIEF JUDGE
    // GET ONE
    // =====================================================

    @Override
    public ParticipantResponse getParticipantById(Long id) {

        return mapToResponse(
                findParticipant(id)
        );
    }


    // =====================================================
    // ASSOCIATION
    // UPDATE
    // =====================================================

    @Override
    @Transactional
    public ParticipantResponse updateParticipant(
            Long id,
            ParticipantRequest request
    ) {

        Participant participant =
                findParticipant(id);

        participant.setFullName(
                request.getFullName()
        );

        participant.setGender(
                request.getGender()
        );

        participant.setAge(
                request.getAge()
        );

        participant.setJuzuu(
                request.getJuzuu()
        );

        participant.setCompetition(
                findCompetition(
                        request.getCompetitionId()
                )
        );

        participant.setMadrasa(
                findMadrasa(
                        request.getMadrasaId()
                )
        );

        participantRepository.save(participant);

        return mapToResponse(participant);
    }


    // =====================================================
    // ASSOCIATION
    // DELETE
    // =====================================================

    @Override
    @Transactional
    public void deleteParticipant(Long id) {

        participantRepository.delete(
                findParticipant(id)
        );
    }


    // =====================================================
    // MADRASA
    // GET OWN PARTICIPANTS
    // =====================================================

    @Override
    public List<ParticipantResponse> getParticipantsForMadrasa(
            String username
    ) {

        Madrasa madrasa =
                findMadrasaByUsername(username);

        return participantRepository
                .findByMadrasa(madrasa)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // MADRASA
    // CREATE OWN PARTICIPANT
    // =====================================================

    @Override
    @Transactional
    public ParticipantResponse createParticipantForMadrasa(
            String username,
            ParticipantRequest request
    ) {

        Madrasa madrasa =
                findMadrasaByUsername(username);

        Competition competition =
                findCompetition(
                        request.getCompetitionId()
                );

        Participant participant =
                Participant.builder()
                        .fullName(request.getFullName())
                        .gender(request.getGender())
                        .age(request.getAge())
                        .juzuu(request.getJuzuu())
                        .competition(competition)
                        .madrasa(madrasa)
                        .build();

        participantRepository.save(participant);

        return mapToResponse(participant);
    }


    // =====================================================
    // MADRASA
    // UPDATE OWN PARTICIPANT
    // =====================================================

    @Override
    @Transactional
    public ParticipantResponse updateParticipantForMadrasa(
            String username,
            Long id,
            ParticipantRequest request
    ) {

        Madrasa madrasa =
                findMadrasaByUsername(username);

        Participant participant =
                findParticipant(id);


        // =================================================
        // SECURITY CHECK
        // Participant lazima awe wa Madrasa huyu
        // =================================================

        if (!participant.getMadrasa().getId()
                .equals(madrasa.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not authorized to update this participant."
            );
        }


        participant.setFullName(
                request.getFullName()
        );

        participant.setGender(
                request.getGender()
        );

        participant.setAge(
                request.getAge()
        );

        participant.setJuzuu(
                request.getJuzuu()
        );


        // Competition inaweza kubadilishwa
        // lakini madrasa HAIBADILISHWI

        participant.setCompetition(
                findCompetition(
                        request.getCompetitionId()
                )
        );


        participantRepository.save(participant);

        return mapToResponse(participant);
    }


    // =====================================================
    // MADRASA
    // DELETE OWN PARTICIPANT
    // =====================================================

    @Override
    @Transactional
    public void deleteParticipantForMadrasa(
            String username,
            Long id
    ) {

        Madrasa madrasa =
                findMadrasaByUsername(username);

        Participant participant =
                findParticipant(id);


        // =================================================
        // SECURITY CHECK
        // =================================================

        if (!participant.getMadrasa().getId()
                .equals(madrasa.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not authorized to delete this participant."
            );
        }


        participantRepository.delete(participant);
    }


    // =====================================================
    // HELPERS
    // =====================================================

    private Participant findParticipant(Long id) {

        return participantRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Participant not found."
                        )
                );
    }


    private Competition findCompetition(Long id) {

        return competitionRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Competition not found."
                        )
                );
    }


    private Madrasa findMadrasa(Long id) {

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
    // FIND MADRASA USING LOGGED-IN USERNAME
    // =====================================================

    private Madrasa findMadrasaByUsername(
            String username
    ) {

        /*
         * HAPA NDIPO TUNAHITAJI KUUNGANISHA
         * username ya JWT na Madrasa.
         *
         * Mfano:
         *
         * MDR001 -> Al-Huda Madrasa
         *
         * Method ya repository itategemea structure
         * ya Madrasa entity yako.
         */

        return madrasaRepository
                .findByUser_Username(username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Madrasa account not found."
                        )
                );
    }


    // =====================================================
    // MAP RESPONSE
    // =====================================================

    private ParticipantResponse mapToResponse(
            Participant participant
    ) {

        return ParticipantResponse.builder()
                .id(participant.getId())
                .fullName(participant.getFullName())
                .gender(participant.getGender())
                .age(participant.getAge())
                .juzuu(participant.getJuzuu())
                .status(participant.getStatus())
                .competition(
                        participant
                                .getCompetition()
                                .getTitle()
                )
                .madrasa(
                        participant
                                .getMadrasa()
                                .getName()
                )
                .build();
    }

}