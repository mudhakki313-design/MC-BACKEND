package com.qmcms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {

    private Long id;

    private String fullName;

    private String username;

    private String email;

    private String role;

    private String status;

    private String profileImage;

    private LocalDateTime createdAt;
}