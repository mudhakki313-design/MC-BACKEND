package com.qmcms.controller.madrasa;

import com.qmcms.dto.request.MadrasaRequest;
import com.qmcms.dto.response.MadrasaResponse;
import com.qmcms.service.MadrasaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/madrasas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MadrasaController {

    private final MadrasaService madrasaService;

    @PostMapping
    @PreAuthorize("hasRole('ASSOCIATION')")
    public MadrasaResponse createMadrasa(
            @Valid @RequestBody MadrasaRequest request) {

        return madrasaService.createMadrasa(request);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ASSOCIATION', 'CHIEF_JUDGE')")
    public List<MadrasaResponse> getAllMadrasas() {

        return madrasaService.getAllMadrasas();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public MadrasaResponse getMadrasaById(
            @PathVariable Long id) {

        return madrasaService.getMadrasaById(id);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public MadrasaResponse updateMadrasa(
            @PathVariable Long id,
            @Valid @RequestBody MadrasaRequest request) {

        return madrasaService.updateMadrasa(id, request);

    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ASSOCIATION')")
    public void deactivateMadrasa(
            @PathVariable Long id) {

        madrasaService.deactivateMadrasa(id);

    }

}