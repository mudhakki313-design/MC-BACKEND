package com.qmcms.dto.response;

import com.qmcms.entity.JudgeType;
import com.qmcms.entity.Juzuu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScoreResponse {

    private Long id;

    private Long participantId;

    private String participant;

    private String madrasa;

    private String competition;

    private Juzuu juzuu;

    private String judge;

    private JudgeType judgeType;

    private Double score;
    private Long competitionId;
}