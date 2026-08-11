package com.qmcms.dto.request;

import com.qmcms.entity.MadrasaStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MadrasaRequest {

    @NotBlank(message = "Madrasa name is required")
    private String name;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Region is required")
    private String region;

    @NotBlank(message = "District is required")
    private String district;

    @NotBlank(message = "Contact person is required")
    private String contactPerson;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email")
    private String email;

    private String address;

    @NotNull(message = "Status is required")
    private MadrasaStatus status;

}