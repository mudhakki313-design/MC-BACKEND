package com.qmcms.controller.result;

import com.qmcms.dto.response.ResultResponse;
import com.qmcms.entity.Juzuu;
import com.qmcms.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/competition/{competitionId}/juzuu/{juzuu}")
    @PreAuthorize("hasAnyRole('ASSOCIATION','CHIEF_JUDGE')")
    public List<ResultResponse> getCompetitionResults(
            @PathVariable Long competitionId,
            @PathVariable Juzuu juzuu) {

        return resultService.getCompetitionResults(competitionId, juzuu);

    }

}