package com.qmcms.controller.screening;

import com.qmcms.dto.response.ParticipantResponse;
import com.qmcms.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screening")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ScreeningController {

    private final ScreeningService screeningService;

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public List<ParticipantResponse> getPendingParticipants() {

        return screeningService.getPendingParticipants();

    }

    @GetMapping("/approved")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public List<ParticipantResponse> getApprovedParticipants() {

        return screeningService.getApprovedParticipants();

    }

    @GetMapping("/rejected")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public List<ParticipantResponse> getRejectedParticipants() {

        return screeningService.getRejectedParticipants();

    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public ParticipantResponse approveParticipant(
            @PathVariable Long id) {

        return screeningService.approveParticipant(id);

    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public ParticipantResponse rejectParticipant(
            @PathVariable Long id) {

        return screeningService.rejectParticipant(id);

    }

}