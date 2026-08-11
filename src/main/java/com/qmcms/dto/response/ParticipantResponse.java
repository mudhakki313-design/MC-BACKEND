package com.qmcms.dto.response;

import com.qmcms.entity.Gender;
import com.qmcms.entity.Juzuu;
import com.qmcms.entity.ParticipantStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantResponse {

    private Long id;

    private String fullName;

    private Gender gender;

    private Integer age;

    private Juzuu juzuu;

    private ParticipantStatus status;

    private String madrasa;

    private String competition;

}