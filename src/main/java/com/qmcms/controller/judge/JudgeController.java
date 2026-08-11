package com.qmcms.controller.judge;

import com.qmcms.dto.request.JudgeRequest;
import com.qmcms.dto.response.JudgeResponse;
import com.qmcms.service.JudgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judges")
@RequiredArgsConstructor
@CrossOrigin("*")
public class JudgeController {

    private final JudgeService judgeService;

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

}