package com.qmcms.service;

import com.qmcms.dto.request.FeedbackRequest;
import com.qmcms.dto.response.FeedbackResponse;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse createFeedback(FeedbackRequest request);

    List<FeedbackResponse> getCompetitionFeedback(Long competitionId);
}