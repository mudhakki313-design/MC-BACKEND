package com.qmcms.service;

import com.qmcms.dto.request.ResultUpdateRequest;
import com.qmcms.dto.response.ResultResponse;
import com.qmcms.entity.*;
import com.qmcms.repository.ParticipantRepository;
import com.qmcms.repository.ResultRepository;
import com.qmcms.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ParticipantRepository participantRepository;
    private final ScoreRepository scoreRepository;
    private final ResultRepository resultRepository;


    // =========================================================
    // CHIEF JUDGE - GENERATE RESULTS
    // =========================================================

    @Override
    @Transactional
    public List<ResultResponse> generateResults(
            Long competitionId,
            Juzuu juzuu
    ) {

        List<Participant> participants =
                participantRepository
                        .findByCompetitionIdAndJuzuuAndStatus(
                                competitionId,
                                juzuu,
                                ParticipantStatus.APPROVED
                        );


        if (participants.isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No approved participants found."
            );
        }


        List<ResultCalculation> calculations =
                new ArrayList<>();


        // =====================================================
        // CHECK EVERY PARTICIPANT
        // =====================================================

        for (Participant participant : participants) {

            List<Score> scores =
                    scoreRepository.findByParticipant(
                            participant
                    );


            boolean memorization =
                    scores.stream()
                            .anyMatch(score ->
                                    score.getJudge()
                                            .getJudgeType()
                                            == JudgeType.MEMORIZATION
                            );


            boolean tajweed =
                    scores.stream()
                            .anyMatch(score ->
                                    score.getJudge()
                                            .getJudgeType()
                                            == JudgeType.TAJWEED
                            );


            boolean makharij =
                    scores.stream()
                            .anyMatch(score ->
                                    score.getJudge()
                                            .getJudgeType()
                                            == JudgeType.MAKHARIJ
                            );


            if (!memorization ||
                    !tajweed ||
                    !makharij) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Not all participants have completed "
                                + "all three judging scores."
                );
            }


            // =================================================
            // CALCULATE TOTAL
            // =================================================

            double total =
                    scores.stream()
                            .filter(score -> {

                                JudgeType type =
                                        score.getJudge()
                                                .getJudgeType();

                                return type == JudgeType.MEMORIZATION
                                        || type == JudgeType.TAJWEED
                                        || type == JudgeType.MAKHARIJ;
                            })
                            .mapToDouble(Score::getScore)
                            .sum();


            calculations.add(
                    new ResultCalculation(
                            participant,
                            total
                    )
            );
        }


        // =====================================================
        // SORT HIGH → LOW
        // =====================================================

        calculations.sort(
                Comparator.comparing(
                        ResultCalculation::totalScore
                ).reversed()
        );


        // =====================================================
        // REMOVE OLD RESULTS
        // =====================================================

        resultRepository
                .deleteByCompetitionIdAndJuzuu(
                        competitionId,
                        juzuu
                );


        // =====================================================
        // SAVE NEW RESULTS
        // =====================================================

        List<Result> savedResults =
                new ArrayList<>();


        int rank = 1;


        for (ResultCalculation calculation :
                calculations) {

            Participant participant =
                    calculation.participant();


            Result result =
                    Result.builder()

                            .participant(participant)

                            .competition(
                                    participant.getCompetition()
                            )

                            .juzuu(
                                    participant.getJuzuu()
                            )

                            .totalScore(
                                    calculation.totalScore()
                            )

                            .rank(rank++)

                            .build();


            savedResults.add(
                    resultRepository.save(result)
            );
        }


        return savedResults
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET SAVED RESULTS
    // =========================================================

    @Override
    public List<ResultResponse> getCompetitionResults(
            Long competitionId,
            Juzuu juzuu
    ) {

        return resultRepository
                .findByCompetitionIdAndJuzuuOrderByRankAsc(
                        competitionId,
                        juzuu
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // CHIEF JUDGE - UPDATE RESULT
    // =========================================================

    @Override
    @Transactional
    public ResultResponse updateResult(
            Long resultId,
            ResultUpdateRequest request
    ) {

        Result result =
                resultRepository
                        .findById(resultId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Result not found."
                                )
                        );


        // =====================================================
        // UPDATE TOTAL SCORE
        // =====================================================

        if (request.getTotalScore() != null) {

            if (request.getTotalScore() < 0) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Total score cannot be negative."
                );
            }

            if (request.getTotalScore() > 100) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Total score cannot exceed 100."
                );
            }

            result.setTotalScore(
                    request.getTotalScore()
            );
        }


        // =====================================================
        // UPDATE RANK
        // =====================================================

        if (request.getRank() != null) {

            if (request.getRank() < 1) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Rank must be greater than zero."
                );
            }

            result.setRank(
                    request.getRank()
            );
        }


        Result savedResult =
                resultRepository.save(result);


        return mapToResponse(savedResult);
    }


    // =========================================================
    // RESULT RESPONSE
    // =========================================================

    private ResultResponse mapToResponse(
            Result result
    ) {

        Participant participant =
                result.getParticipant();


        return ResultResponse.builder()

                .resultId(
                        result.getId()
                )

                .rank(
                        result.getRank()
                )

                .participantId(
                        participant.getId()
                )

                .participant(
                        participant.getFullName()
                )

                .madrasa(
                        participant
                                .getMadrasa()
                                .getName()
                )

                .competition(
                        participant
                                .getCompetition()
                                .getTitle()
                )

                .competitionId(
                        participant
                                .getCompetition()
                                .getId()
                )

                .juzuu(
                        participant.getJuzuu()
                )

                .totalScore(
                        result.getTotalScore()
                )

                .build();
    }


    // =========================================================
    // INTERNAL CALCULATION
    // =========================================================

    private record ResultCalculation(
            Participant participant,
            double totalScore
    ) {}
}