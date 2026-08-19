package com.qmcms.controller.judge;

import com.qmcms.dto.request.JudgeRequest;
import com.qmcms.dto.response.JudgeResponse;
import com.qmcms.entity.Judge;
import com.qmcms.repository.JudgeRepository;
import com.qmcms.service.JudgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/judges")
@RequiredArgsConstructor
@CrossOrigin("*")
public class JudgeController {

    private final JudgeService judgeService;
    private final JudgeRepository judgeRepository;
    @PostMapping
    @PreAuthorize("hasRole('ASSOCIATION')")
    public JudgeResponse createJudge(
            @Valid @RequestBody JudgeRequest request) {

        return judgeService.createJudge(request);

    }

    @GetMapping
    @PreAuthorize("hasRole('ASSOCIATION')")
    public List<JudgeResponse> getAllJudges() {

        return judgeService.getAllJudges();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public JudgeResponse getJudgeById(
            @PathVariable Long id) {

        return judgeService.getJudgeById(id);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public JudgeResponse updateJudge(
            @PathVariable Long id,
            @Valid @RequestBody JudgeRequest request) {

        return judgeService.updateJudge(id, request);

    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public void deactivateJudge(
            @PathVariable Long id) {

        judgeService.deactivateJudge(id);

    }

    // =========================================================
    // GET CURRENT LOGGED-IN JUDGE
    // =========================================================

    @GetMapping("/me")
    public Judge getCurrentJudge(Authentication authentication) {

        if (authentication == null ||
                authentication.getName() == null) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not authenticated."
            );
        }

        return judgeRepository
                .findByUserUsername(authentication.getName())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Judge profile not found."
                        )
                );
    }

}