package com.qmcms.service;

import com.qmcms.dto.request.JudgeRequest;
import com.qmcms.dto.response.JudgeResponse;

import java.util.List;

public interface JudgeService {

    JudgeResponse createJudge(JudgeRequest request);

    List<JudgeResponse> getAllJudges();

    JudgeResponse getJudgeById(Long id);

    JudgeResponse updateJudge(Long id, JudgeRequest request);

    void deactivateJudge(Long id);

}