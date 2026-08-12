package com.qmcms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackRequest {

    @NotNull(message = "Competition is required")
    private Long competitionId;

    @NotBlank(message = "Comment is required")
    private String comment;
}