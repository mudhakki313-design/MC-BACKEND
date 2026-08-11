package com.qmcms.controller.participant;

import com.qmcms.dto.request.ParticipantRequest;
import com.qmcms.dto.response.ParticipantResponse;
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

    @PostMapping
    @PreAuthorize("hasRole('ASSOCIATION')")
    public ParticipantResponse createParticipant(
            @Valid @RequestBody ParticipantRequest request) {

        return participantService.createParticipant(request);

    }

    @GetMapping
    @PreAuthorize("hasRole('ASSOCIATION')")
    public List<ParticipantResponse> getAllParticipants() {

        return participantService.getAllParticipants();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public ParticipantResponse getParticipantById(
            @PathVariable Long id) {

        return participantService.getParticipantById(id);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public ParticipantResponse updateParticipant(
            @PathVariable Long id,
            @Valid @RequestBody ParticipantRequest request) {

        return participantService.updateParticipant(id, request);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public void deleteParticipant(
            @PathVariable Long id) {

        participantService.deleteParticipant(id);

    }

}