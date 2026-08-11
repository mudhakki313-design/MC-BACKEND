package com.qmcms.service;

import com.qmcms.dto.request.CompetitionRequest;
import com.qmcms.dto.response.CompetitionResponse;

import java.util.List;

public interface CompetitionService {

    CompetitionResponse createCompetition(CompetitionRequest request);

    List<CompetitionResponse> getAllCompetitions();

    CompetitionResponse getCompetitionById(Long id);

    CompetitionResponse updateCompetition(Long id, CompetitionRequest request);

    void deleteCompetition(Long id);

}