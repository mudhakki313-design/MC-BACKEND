package com.qmcms.service;

import com.qmcms.dto.request.MadrasaRequest;
import com.qmcms.dto.response.MadrasaResponse;

import java.util.List;

public interface MadrasaService {


    // =====================================================
    // CREATE
    // =====================================================

    MadrasaResponse createMadrasa(
            MadrasaRequest request
    );


    // =====================================================
    // GET ALL
    // =====================================================

    List<MadrasaResponse> getAllMadrasas();


    // =====================================================
    // GET ONE
    // =====================================================

    MadrasaResponse getMadrasaById(
            Long id
    );


    // =====================================================
    // GET MY MADRASA
    // =====================================================

    MadrasaResponse getMyMadrasa();


    // =====================================================
    // UPDATE
    // =====================================================

    MadrasaResponse updateMadrasa(
            Long id,
            MadrasaRequest request
    );


    // =====================================================
    // DEACTIVATE
    // =====================================================

    void deactivateMadrasa(
            Long id
    );

}