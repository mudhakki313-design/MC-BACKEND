package com.qmcms.service;

import com.qmcms.dto.response.ResultResponse;
import com.qmcms.entity.*;
import com.qmcms.repository.ParticipantRepository;
import com.qmcms.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ParticipantRepository participantRepository;
    private final ScoreRepository scoreRepository;

    @Override
    public List<ResultResponse> getCompetitionResults(Long competitionId,
                                                      Juzuu juzuu) {

        List<Participant> participants =
                participantRepository.findByCompetitionIdAndJuzuuAndStatus(
                        competitionId,
                        juzuu,
                        ParticipantStatus.APPROVED
                );

        List<ResultResponse> results = new ArrayList<>();

        for (Participant participant : participants) {

            // Pata scores zote za participant
            List<Score> scores = scoreRepository.findByParticipant(participant);

            // Hakikisha participant ana score kutoka kwa Judges wote watatu
            boolean hasMemorization = scores.stream()
                    .anyMatch(score ->
                            score.getJudge().getJudgeType() == JudgeType.MEMORIZATION);

            boolean hasTajweed = scores.stream()
                    .anyMatch(score ->
                            score.getJudge().getJudgeType() == JudgeType.TAJWEED);

            boolean hasMakharij = scores.stream()
                    .anyMatch(score ->
                            score.getJudge().getJudgeType() == JudgeType.MAKHARIJ);

            // Akiwa hajapata score zote tatu, asionekane kwenye results
            if (!(hasMemorization && hasTajweed && hasMakharij)) {
                continue;
            }

            // Jumlisha alama zote
            double total = scores.stream()
                    .mapToDouble(Score::getScore)
                    .sum();

            results.add(
                    ResultResponse.builder()
                            .participantId(participant.getId())
                            .participant(participant.getFullName())
                            .madrasa(participant.getMadrasa().getName())
                            .competition(participant.getCompetition().getTitle())
                            .juzuu(participant.getJuzuu())
                            .totalScore(total)
                            .build()
            );

        }

        // Pangilia kutoka score kubwa kwenda ndogo
        results.sort(
                Comparator.comparing(ResultResponse::getTotalScore)
                        .reversed()
        );

        // Weka ranking
        int rank = 1;
        for (ResultResponse result : results) {
            result.setRank(rank++);
        }

        return results;
    }
}