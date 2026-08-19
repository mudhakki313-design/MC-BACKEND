package com.qmcms.controller.participant;

import com.qmcms.dto.request.ParticipantRequest;
import com.qmcms.dto.response.ParticipantResponse;
import com.qmcms.entity.ParticipantStatus;
import com.qmcms.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ParticipantController {

    private final ParticipantService participantService;


    // =====================================================
    // ASSOCIATION ONLY
    // CREATE PARTICIPANT
    // =====================================================

    @PostMapping
    @PreAuthorize("hasRole('ASSOCIATION')")
    public ParticipantResponse createParticipant(
            @Valid @RequestBody ParticipantRequest request) {

        return participantService.createParticipant(request);
    }


    // =====================================================
    // ASSOCIATION + CHIEF JUDGE
    // VIEW ALL PARTICIPANTS
    // =====================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ASSOCIATION', 'CHIEF_JUDGE')")
    public List<ParticipantResponse> getAllParticipants() {

        return participantService.getAllParticipants();
    }


    // =====================================================
    // ASSOCIATION + CHIEF JUDGE
    // VIEW ONE PARTICIPANT
    // =====================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ASSOCIATION', 'CHIEF_JUDGE')")
    public ParticipantResponse getParticipantById(
            @PathVariable Long id) {

        return participantService.getParticipantById(id);
    }


    // =====================================================
    // ASSOCIATION ONLY
    // UPDATE PARTICIPANT
    // =====================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public ParticipantResponse updateParticipant(
            @PathVariable Long id,
            @Valid @RequestBody ParticipantRequest request) {

        return participantService.updateParticipant(id, request);
    }


    // =====================================================
    // ASSOCIATION ONLY
    // DELETE PARTICIPANT
    // =====================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public void deleteParticipant(
            @PathVariable Long id) {

        participantService.deleteParticipant(id);
    }

    // =========================================================
// JUDGE - VIEW APPROVED PARTICIPANTS
// =========================================================

    @GetMapping("/judge")
    @PreAuthorize("hasAuthority('ROLE_JUDGE')")
    public List<ParticipantResponse> getParticipantsForJudge() {

        return participantService.getAllParticipants()
                .stream()
                .filter(participant ->
                        participant.getStatus() == ParticipantStatus.APPROVED
                )
                .toList();

    }
}