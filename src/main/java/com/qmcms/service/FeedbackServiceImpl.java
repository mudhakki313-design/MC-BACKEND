package com.qmcms.service;

import com.qmcms.dto.request.FeedbackRequest;
import com.qmcms.dto.response.FeedbackResponse;
import com.qmcms.entity.Competition;
import com.qmcms.entity.Feedback;
import com.qmcms.entity.Madrasa;
import com.qmcms.repository.CompetitionRepository;
import com.qmcms.repository.FeedbackRepository;
import com.qmcms.repository.MadrasaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CompetitionRepository competitionRepository;
    private final MadrasaRepository madrasaRepository;

    @Override
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {

        Competition competition =
                competitionRepository.findById(request.getCompetitionId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Competition not found."
                        ));

        Madrasa madrasa = getCurrentMadrasa();

        if (feedbackRepository.existsByCompetitionAndMadrasa(
                competition,
                madrasa
        )) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Feedback already submitted for this competition."
            );
        }

        Feedback feedback = Feedback.builder()
                .competition(competition)
                .madrasa(madrasa)
                .comment(request.getComment().trim())
                .build();

        feedbackRepository.save(feedback);

        return mapToResponse(feedback);
    }

    @Override
    public List<FeedbackResponse> getCompetitionFeedback(
            Long competitionId) {

        return feedbackRepository
                .findByCompetitionId(competitionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =====================================================
    // Helper Methods
    // =====================================================

    private Madrasa getCurrentMadrasa() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                authentication.getName() == null) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not authenticated."
            );
        }

        String username = authentication.getName();

        return madrasaRepository
                .findByUser_Username(username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Madrasa account not found."
                        )
                );
    }

    private FeedbackResponse mapToResponse(Feedback feedback) {

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .madrasa(feedback.getMadrasa().getName())
                .competition(feedback.getCompetition().getTitle())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}