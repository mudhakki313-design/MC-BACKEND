package com.qmcms.service;

import com.qmcms.dto.response.ParticipantResponse;
import com.qmcms.entity.Participant;
import com.qmcms.entity.ParticipantStatus;
import com.qmcms.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningServiceImpl implements ScreeningService {

    private final ParticipantRepository participantRepository;

    @Override
    public List<ParticipantResponse> getPendingParticipants() {

        return participantRepository.findByStatus(ParticipantStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<ParticipantResponse> getApprovedParticipants() {

        return participantRepository.findByStatus(ParticipantStatus.APPROVED)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<ParticipantResponse> getRejectedParticipants() {

        return participantRepository.findByStatus(ParticipantStatus.REJECTED)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    @Transactional
    public ParticipantResponse approveParticipant(Long id) {

        Participant participant = findParticipant(id);

        participant.setStatus(ParticipantStatus.APPROVED);

        participantRepository.save(participant);

        return mapToResponse(participant);

    }

    @Override
    @Transactional
    public ParticipantResponse rejectParticipant(Long id) {

        Participant participant = findParticipant(id);

        participant.setStatus(ParticipantStatus.REJECTED);

        participantRepository.save(participant);

        return mapToResponse(participant);

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