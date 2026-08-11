package com.qmcms.controller.competition;

import com.qmcms.dto.request.CompetitionRequest;
import com.qmcms.dto.response.CompetitionResponse;
import com.qmcms.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CompetitionController {

    private final CompetitionService competitionService;

    @PostMapping
    @PreAuthorize("hasRole('ASSOCIATION')")
    public CompetitionResponse createCompetition(
            @Valid @RequestBody CompetitionRequest request) {

        return competitionService.createCompetition(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ASSOCIATION','MADRASA','JUDGE','CHIEF_JUDGE')")
    public List<CompetitionResponse> getAllCompetitions() {

        return competitionService.getAllCompetitions();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ASSOCIATION','MADRASA','JUDGE','CHIEF_JUDGE')")
    public CompetitionResponse getCompetitionById(
            @PathVariable Long id) {

        return competitionService.getCompetitionById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public CompetitionResponse updateCompetition(
            @PathVariable Long id,
            @Valid @RequestBody CompetitionRequest request) {

        return competitionService.updateCompetition(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public void deleteCompetition(
            @PathVariable Long id) {

        competitionService.deleteCompetition(id);
    }

}