package com.qmcms.dto.response;

import com.qmcms.entity.Juzuu;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultResponse {

    private Long resultId;

    private Integer rank;

    private Long participantId;

    private String participant;

    private String madrasa;

    private String competition;

    private Long competitionId;

    private Juzuu juzuu;

    private Double totalScore;
}