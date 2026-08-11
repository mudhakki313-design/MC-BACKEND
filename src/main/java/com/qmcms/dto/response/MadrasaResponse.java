package com.qmcms.dto.response;

import com.qmcms.entity.MadrasaStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MadrasaResponse {

    private Long id;

    private String name;

    private String registrationNumber;

    private String region;

    private String district;

    private String contactPerson;

    private String phone;

    private String email;

    private String address;

    private MadrasaStatus status;

    private String username;

    private LocalDateTime createdAt;

}