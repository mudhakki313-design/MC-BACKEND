package com.qmcms.service;

import com.qmcms.dto.request.ScoreRequest;
import com.qmcms.dto.response.ScoreResponse;

import java.util.List;

public interface ScoreService {

    ScoreResponse createScore(ScoreRequest request);

    List<ScoreResponse> getAllScores();

    List<ScoreResponse> getParticipantScores(Long participantId);

}