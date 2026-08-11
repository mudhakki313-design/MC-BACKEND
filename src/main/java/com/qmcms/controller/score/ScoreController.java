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

    @PostMapping
    @PreAuthorize("hasRole('JUDGE')")
    public ScoreResponse createScore(
            @Valid @RequestBody ScoreRequest request) {

        return scoreService.createScore(request);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ASSOCIATION','CHIEF_JUDGE')")
    public List<ScoreResponse> getAllScores() {

        return scoreService.getAllScores();

    }

    @GetMapping("/participant/{participantId}")
    @PreAuthorize("hasAnyRole('ASSOCIATION','CHIEF_JUDGE','JUDGE')")
    public List<ScoreResponse> getParticipantScores(
            @PathVariable Long participantId) {

        return scoreService.getParticipantScores(participantId);

    }

}