package com.qmcms.service;

import com.qmcms.dto.response.ResultResponse;
import com.qmcms.entity.Juzuu;

import java.util.List;

public interface ResultService {

    List<ResultResponse> getCompetitionResults(Long competitionId, Juzuu juzuu);

}