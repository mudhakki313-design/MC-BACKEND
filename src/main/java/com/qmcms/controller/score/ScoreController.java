package com.qmcms.controller.score;

import com.qmcms.dto.request.ScoreRequest;
import com.qmcms.dto.response.ScoreResponse;
import com.qmcms.service.ScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ScoreController {

    private final ScoreService scoreService;

    // ==========================================
    // JUDGE - Submit his/her component score
    // ==========================================

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_JUDGE')")
    public ScoreResponse createScore(
            @Valid @RequestBody ScoreRequest request) {

        return scoreService.createScore(request);
    }


    // ==========================================
    // ASSOCIATION + CHIEF JUDGE
    // View all scores
    // ==========================================

    @GetMapping
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_ASSOCIATION',
                'ROLE_CHIEF_JUDGE'
            )
            """)
    public List<ScoreResponse> getAllScores() {

        return scoreService.getAllScores();
    }


    // ==========================================
    // View participant scores
    // Judge sees ONLY own score
    // Chief/Association see ALL scores
    // ==========================================

    @GetMapping("/participant/{participantId}")
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_ASSOCIATION',
                'ROLE_CHIEF_JUDGE',
                'ROLE_JUDGE'
            )
            """)
    public List<ScoreResponse> getParticipantScores(
            @PathVariable Long participantId) {

        return scoreService.getParticipantScores(participantId);
    }
}