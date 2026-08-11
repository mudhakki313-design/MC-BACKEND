package com.qmcms.dto.response;

import com.qmcms.entity.JudgeType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreResponse {

    private Long id;

    private String participant;

    private String madrasa;

    private String judge;

    private JudgeType judgeType;

    private Double score;

}