package com.qmcms.controller.feedback;

import com.qmcms.dto.request.FeedbackRequest;
import com.qmcms.dto.response.FeedbackResponse;
import com.qmcms.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MADRASA')")
    public FeedbackResponse createFeedback(
            @Valid @RequestBody FeedbackRequest request) {

        return feedbackService.createFeedback(request);
    }

    @GetMapping("/competition/{competitionId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ASSOCIATION')")
    public List<FeedbackResponse> getCompetitionFeedback(
            @PathVariable Long competitionId) {

        return feedbackService.getCompetitionFeedback(
                competitionId
        );
    }
}