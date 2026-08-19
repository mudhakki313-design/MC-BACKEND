package com.qmcms.service;

import com.qmcms.dto.request.ResultUpdateRequest;
import com.qmcms.dto.response.ResultResponse;
import com.qmcms.entity.Juzuu;

import java.util.List;

public interface ResultService {

    // =========================================================
    // CHIEF JUDGE - GENERATE RESULTS
    // =========================================================

    List<ResultResponse> generateResults(
            Long competitionId,
            Juzuu juzuu
    );


    // =========================================================
    // GET SAVED RESULTS
    // =========================================================

    List<ResultResponse> getCompetitionResults(
            Long competitionId,
            Juzuu juzuu
    );


    // =========================================================
    // CHIEF JUDGE - UPDATE RESULT
    // =========================================================

    ResultResponse updateResult(
            Long resultId,
            ResultUpdateRequest request
    );
}