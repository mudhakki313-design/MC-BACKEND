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

    @Override
    @Transactional
    public ParticipantResponse createParticipant(ParticipantRequest request) {

        Competition competition = findCompetition(request.getCompetitionId());

        Madrasa madrasa = findMadrasa(request.getMadrasaId());

        Participant participant = Participant.builder()
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

    @Override
    public List<ParticipantResponse> getAllParticipants() {

        return participantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public ParticipantResponse getParticipantById(Long id) {

        return mapToResponse(findParticipant(id));

    }

    @Override
    @Transactional
    public ParticipantResponse updateParticipant(Long id,
                                                 ParticipantRequest request) {

        Participant participant = findParticipant(id);

        participant.setFullName(request.getFullName());
        participant.setGender(request.getGender());
        participant.setAge(request.getAge());
        participant.setJuzuu(request.getJuzuu());

        participant.setCompetition(findCompetition(request.getCompetitionId()));
        participant.setMadrasa(findMadrasa(request.getMadrasaId()));

        participantRepository.save(participant);

        return mapToResponse(participant);

    }

    @Override
    @Transactional
    public void deleteParticipant(Long id) {

        participantRepository.delete(findParticipant(id));

    }

    // ==========================
    // Helper Methods
    // ==========================

    private Participant findParticipant(Long id) {

        return participantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Participant not found."
                ));

    }

    private Competition findCompetition(Long id) {

        return competitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Competition not found."
                ));

    }

    private Madrasa findMadrasa(Long id) {

        return madrasaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Madrasa not found."
                ));

    }

    private ParticipantResponse mapToResponse(Participant participant) {

        return ParticipantResponse.builder()
                .id(participant.getId())
                .fullName(participant.getFullName())
                .gender(participant.getGender())
                .age(participant.getAge())
                .juzuu(participant.getJuzuu())
                .status(participant.getStatus())
                .competition(participant.getCompetition().getTitle())
                .madrasa(participant.getMadrasa().getName())
                .build();

    }

}