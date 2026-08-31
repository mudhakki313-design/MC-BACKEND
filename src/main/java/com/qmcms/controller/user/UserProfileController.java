package com.qmcms.controller.user;

import com.qmcms.dto.response.UserProfileResponse;
import com.qmcms.entity.User;
import com.qmcms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;


    // ==========================================
    // GET CURRENT USER PROFILE
    // ==========================================

    @GetMapping
    public ResponseEntity<UserProfileResponse> getMyProfile(
            Authentication authentication
    ) {

        String username = authentication.getName();

        User user = userService
                .getProfileByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ResponseEntity.ok(toResponse(user));
    }


    // ==========================================
    // UPDATE PROFILE DETAILS
    // ==========================================

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateMyProfile(

            Authentication authentication,

            @RequestBody UserProfileUpdateRequest request

    ) {

        String username = authentication.getName();

        User user = userService
                .getProfileByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        // --------------------------------------
        // UPDATE FULL NAME
        // --------------------------------------

        if (request.fullName() != null &&
                !request.fullName().isBlank()) {

            user.setFullName(
                    request.fullName().trim()
            );
        }


        // --------------------------------------
        // UPDATE EMAIL
        // --------------------------------------

        if (request.email() != null &&
                !request.email().isBlank()) {

            user.setEmail(
                    request.email().trim()
            );
        }


        User savedUser =
                userService.save(user);

        return ResponseEntity.ok(
                toResponse(savedUser)
        );
    }


    // ==========================================
    // UPLOAD PROFILE IMAGE
    // ==========================================

    @PostMapping(
            value = "/image",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<UserProfileResponse> uploadProfileImage(

            Authentication authentication,

            @RequestParam("file") MultipartFile file

    ) throws IOException {

        if (file == null || file.isEmpty()) {

            throw new RuntimeException(
                    "Profile image is required"
            );
        }


        // --------------------------------------
        // VALIDATE IMAGE TYPE
        // --------------------------------------

        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {

            throw new RuntimeException(
                    "Only image files are allowed"
            );
        }


        // --------------------------------------
        // MAX 5MB
        // --------------------------------------

        if (file.getSize() > 5 * 1024 * 1024) {

            throw new RuntimeException(
                    "Profile image must not exceed 5MB"
            );
        }


        // --------------------------------------
        // GET CURRENT USER
        // --------------------------------------

        String username =
                authentication.getName();

        User user =
                userService
                        .getProfileByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );


        // --------------------------------------
        // CONVERT IMAGE TO BASE64
        // --------------------------------------

        String contentType =
                file.getContentType();

        String base64 =
                Base64.getEncoder()
                        .encodeToString(
                                file.getBytes()
                        );


        String imageData =
                "data:" +
                        contentType +
                        ";base64," +
                        base64;


        // --------------------------------------
        // SAVE IMAGE
        // --------------------------------------

        user.setProfileImage(imageData);

        User savedUser =
                userService.save(user);


        // --------------------------------------
        // RESPONSE
        // --------------------------------------

        return ResponseEntity.ok(
                toResponse(savedUser)
        );
    }


    // ==========================================
    // RESPONSE MAPPER
    // ==========================================

    private UserProfileResponse toResponse(
            User user
    ) {

        return UserProfileResponse.builder()

                .id(user.getId())

                .fullName(user.getFullName())

                .username(user.getUsername())

                .email(user.getEmail())

                .role(user.getRole().name())

                .status(user.getStatus().name())

                .profileImage(
                        user.getProfileImage()
                )

                .createdAt(
                        user.getCreatedAt()
                )

                .build();
    }


    // ==========================================
    // UPDATE REQUEST
    // ==========================================

    public record UserProfileUpdateRequest(

            String fullName,

            String email,

            String profileImage

    ) {}
}