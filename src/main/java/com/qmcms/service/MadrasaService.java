package com.qmcms.service;

import com.qmcms.dto.request.MadrasaRequest;
import com.qmcms.dto.response.MadrasaResponse;

import java.util.List;

public interface MadrasaService {

    MadrasaResponse createMadrasa(MadrasaRequest request);

    List<MadrasaResponse> getAllMadrasas();

    MadrasaResponse getMadrasaById(Long id);

    MadrasaResponse updateMadrasa(Long id, MadrasaRequest request);

    void deactivateMadrasa(Long id);

}