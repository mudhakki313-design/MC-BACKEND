package com.qmcms.controller.result;

import com.qmcms.dto.request.ResultUpdateRequest;
import com.qmcms.dto.response.ResultResponse;
import com.qmcms.entity.Juzuu;
import com.qmcms.service.ResultService;
import jakarta.validation.Valid;
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


    // =========================================================
    // CHIEF JUDGE - GENERATE RESULTS
    // =========================================================

    @PostMapping(
            "/competition/{competitionId}/juzuu/{juzuu}/generate"
    )
    @PreAuthorize("hasAuthority('ROLE_CHIEF_JUDGE')")
    public List<ResultResponse> generateResults(

            @PathVariable Long competitionId,

            @PathVariable Juzuu juzuu

    ) {

        return resultService.generateResults(
                competitionId,
                juzuu
        );
    }


    // =========================================================
    // MADRASA + ASSOCIATION + CHIEF JUDGE
    // GET SAVED RESULTS
    // =========================================================

    @GetMapping(
            "/competition/{competitionId}/juzuu/{juzuu}"
    )
    @PreAuthorize("""
            hasAnyAuthority(
                'ROLE_MADRASA',
                'ROLE_ASSOCIATION',
                'ROLE_CHIEF_JUDGE'
            )
            """)
    public List<ResultResponse> getCompetitionResults(

            @PathVariable Long competitionId,

            @PathVariable Juzuu juzuu

    ) {

        return resultService.getCompetitionResults(
                competitionId,
                juzuu
        );
    }


    // =========================================================
    // CHIEF JUDGE - UPDATE RESULT
    // =========================================================

    @PutMapping("/{resultId}")
    @PreAuthorize("hasAuthority('ROLE_CHIEF_JUDGE')")
    public ResultResponse updateResult(

            @PathVariable Long resultId,

            @Valid @RequestBody ResultUpdateRequest request

    ) {

        return resultService.updateResult(
                resultId,
                request
        );
    }
}