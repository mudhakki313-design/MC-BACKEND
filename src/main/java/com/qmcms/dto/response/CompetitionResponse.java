package com.qmcms.dto.response;

import com.qmcms.entity.CompetitionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class CompetitionResponse {

    private Long id;

    private String title;

    private String venue;

    private LocalDate competitionDate;

    private CompetitionStatus status;

    private LocalDateTime createdAt;

}