package com.qmcms.service;

import com.qmcms.dto.response.ParticipantResponse;

import java.util.List;

public interface ScreeningService {

    List<ParticipantResponse> getPendingParticipants();

    List<ParticipantResponse> getApprovedParticipants();

    List<ParticipantResponse> getRejectedParticipants();

    ParticipantResponse approveParticipant(Long id);

    ParticipantResponse rejectParticipant(Long id);

}