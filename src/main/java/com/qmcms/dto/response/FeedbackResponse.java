package com.qmcms.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FeedbackResponse {

    private Long id;

    private String madrasa;

    private String competition;

    private String comment;

    private LocalDateTime createdAt;
}