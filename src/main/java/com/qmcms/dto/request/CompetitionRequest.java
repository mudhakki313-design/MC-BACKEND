package com.qmcms.dto.request;

import com.qmcms.entity.CompetitionStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionRequest {

    @NotBlank(message = "Competition title is required")
    private String title;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotNull(message = "Competition date is required")
    @FutureOrPresent(message = "Competition date must be today or in the future")
    private LocalDate competitionDate;

    @NotNull(message = "Competition status is required")
    private CompetitionStatus status;

}