package com.qmcms.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScoreRequest {

    @NotNull(message = "Participant is required")
    private Long participantId;

    @NotNull(message = "Judge is required")
    private Long judgeId;

    @NotNull(message = "Score is required")
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    private Double score;

}