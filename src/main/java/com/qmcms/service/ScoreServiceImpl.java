package com.qmcms.service;

import com.qmcms.dto.request.ScoreRequest;
import com.qmcms.dto.response.ScoreResponse;
import com.qmcms.entity.*;
import com.qmcms.repository.JudgeRepository;
import com.qmcms.repository.ParticipantRepository;
import com.qmcms.repository.ScoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final ParticipantRepository participantRepository;
    private final JudgeRepository judgeRepository;

    @Override
    @Transactional
    public ScoreResponse createScore(ScoreRequest request) {

        Participant participant = findParticipant(request.getParticipantId());

        Judge judge = findJudge(request.getJudgeId());

        if (participant.getStatus() != ParticipantStatus.APPROVED) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Participant has not passed screening."
            );

        }

        scoreRepository.findByParticipantAndJudge(participant, judge)
                .ifPresent(score -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Score already submitted."
                    );
                });

        validateScore(judge, request.getScore());

        Score score = Score.builder()
                .participant(participant)
                .judge(judge)
                .score(request.getScore())
                .build();

        scoreRepository.save(score);

        return mapToResponse(score);

    }

    @Override
    public List<ScoreResponse> getAllScores() {

        return scoreRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public List<ScoreResponse> getParticipantScores(Long participantId) {

        Participant participant = findParticipant(participantId);

        return scoreRepository.findByParticipant(participant)
                .stream()
                .map(this::mapToResponse)
                .toList();

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

    private Judge findJudge(Long id) {

        return judgeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Judge not found."
                ));

    }

    private void validateScore(Judge judge, Double score) {

        switch (judge.getJudgeType()) {

            case MEMORIZATION -> {

                if (score > 50) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Maximum Memorization score is 50."
                    );

                }

            }

            case TAJWEED -> {

                if (score > 30) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Maximum Tajweed score is 30."
                    );

                }

            }

            case MAKHARIJ -> {

                if (score > 20) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Maximum Makharij score is 20."
                    );

                }

            }

            case CHIEF -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Chief Judge cannot submit scores."
            );

        }

    }

    private ScoreResponse mapToResponse(Score score) {

        return ScoreResponse.builder()
                .id(score.getId())
                .participant(score.getParticipant().getFullName())
                .madrasa(score.getParticipant().getMadrasa().getName())
                .judge(score.getJudge().getFullName())
                .judgeType(score.getJudge().getJudgeType())
                .score(score.getScore())
                .build();

    }

}