package com.qmcms.dto.request;

import com.qmcms.entity.JudgeStatus;
import com.qmcms.entity.JudgeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JudgeRequest {

    @NotBlank(message = "Judge number is required")
    private String judgeNumber;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email")
    private String email;

    @NotNull(message = "Judge type is required")
    private JudgeType judgeType;

    @NotNull(message = "Status is required")
    private JudgeStatus status;

}