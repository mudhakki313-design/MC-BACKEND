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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;
    private final ParticipantRepository participantRepository;
    private final JudgeRepository judgeRepository;


    // =========================================================
    // JUDGE - CREATE SCORE
    // =========================================================

    @Override
    @Transactional
    public ScoreResponse createScore(ScoreRequest request) {

        Participant participant =
                findParticipant(request.getParticipantId());

        Judge judge = getCurrentJudge();


        // Participant lazima awe approved
        if (participant.getStatus() != ParticipantStatus.APPROVED) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Participant has not passed screening."
            );
        }


        // Judge huyu tayari ameweka score?
        scoreRepository.findByParticipantAndJudge(
                participant,
                judge
        ).ifPresent(score -> {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Score already submitted."
            );

        });


        // Validate score according to judge type
        validateScore(
                judge.getJudgeType(),
                request.getScore()
        );


        Score score = Score.builder()
                .participant(participant)
                .judge(judge)
                .score(request.getScore())
                .build();


        scoreRepository.save(score);


        return mapToResponse(score);
    }


    // =========================================================
    // GET ALL SCORES
    // =========================================================

    @Override
    public List<ScoreResponse> getAllScores() {

        return scoreRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET PARTICIPANT SCORES
    // =========================================================

    @Override
    public List<ScoreResponse> getParticipantScores(
            Long participantId) {

        Participant participant =
                findParticipant(participantId);


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        String role =
                authentication.getAuthorities()
                        .stream()
                        .findFirst()
                        .map(authority -> authority.getAuthority())
                        .orElse("");


        /*
         * Judge wa kawaida anaona
         * score yake tu.
         */
        if ("ROLE_JUDGE".equals(role)) {

            Judge currentJudge =
                    getCurrentJudge();


            return scoreRepository
                    .findByParticipantAndJudge(
                            participant,
                            currentJudge
                    )
                    .map(score ->
                            List.of(mapToResponse(score))
                    )
                    .orElse(List.of());
        }


        /*
         * Chief Judge na Association
         * wanaona scores zote.
         */
        return scoreRepository
                .findByParticipant(participant)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // FIND PARTICIPANT
    // =========================================================

    private Participant findParticipant(Long id) {

        return participantRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Participant not found."
                        )
                );
    }


    // =========================================================
    // GET CURRENT JUDGE
    // =========================================================

    private Judge getCurrentJudge() {

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


        String username =
                authentication.getName();


        return judgeRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Judge profile not found."
                        )
                );
    }


    // =========================================================
    // VALIDATE SCORE
    // =========================================================

    private void validateScore(
            JudgeType judgeType,
            Double score) {

        if (score == null || score < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Score cannot be negative."
            );
        }


        switch (judgeType) {

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
                    "Chief Judge cannot submit component scores."
            );
        }
    }


    // =========================================================
    // SCORE RESPONSE
    // =========================================================

    private ScoreResponse mapToResponse(Score score) {

        Participant participant =
                score.getParticipant();


        return ScoreResponse.builder()

                .id(score.getId())

                .participantId(
                        participant.getId()
                )

                .participant(
                        participant.getFullName()
                )

                .madrasa(
                        participant.getMadrasa().getName()
                )

                .competition(
                        participant
                                .getCompetition()
                                .getTitle()
                )

                .juzuu(
                        participant.getJuzuu()
                )

                .competitionId(
                        participant
                                .getCompetition()
                                .getId()
                )

                .judge(
                        score.getJudge().getFullName()
                )

                .judgeType(
                        score.getJudge().getJudgeType()
                )

                .score(
                        score.getScore()
                )

                .build();
    }
}