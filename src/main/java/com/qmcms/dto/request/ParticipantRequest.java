package com.qmcms.dto.request;

import com.qmcms.entity.Gender;
import com.qmcms.entity.Juzuu;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipantRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Age is required")
    @Min(value = 5)
    @Max(value = 100)
    private Integer age;

    @NotNull(message = "Juzuu is required")
    private Juzuu juzuu;

    @NotNull(message = "Competition is required")
    private Long competitionId;

    @NotNull(message = "Madrasa is required")
    private Long madrasaId;

}