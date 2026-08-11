package com.qmcms.service;

import com.qmcms.dto.request.ParticipantRequest;
import com.qmcms.dto.response.ParticipantResponse;

import java.util.List;

public interface ParticipantService {

    ParticipantResponse createParticipant(ParticipantRequest request);

    List<ParticipantResponse> getAllParticipants();

    ParticipantResponse getParticipantById(Long id);

    ParticipantResponse updateParticipant(Long id, ParticipantRequest request);

    void deleteParticipant(Long id);

}