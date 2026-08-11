package com.qmcms.service;

import com.qmcms.dto.request.JudgeRequest;
import com.qmcms.dto.response.JudgeResponse;
import com.qmcms.entity.*;
import com.qmcms.repository.JudgeRepository;
import com.qmcms.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {

    private final JudgeRepository judgeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public JudgeResponse createJudge(JudgeRequest request) {

        if (judgeRepository.existsByJudgeNumber(request.getJudgeNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Judge number already exists."
            );
        }

        if (judgeRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists."
            );
        }

        if (userRepository.existsByUsername(request.getJudgeNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username already exists."
            );
        }

        Role role = request.getJudgeType() == JudgeType.CHIEF
                ? Role.ROLE_CHIEF_JUDGE
                : Role.ROLE_JUDGE;

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getJudgeNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getJudgeNumber() + "@2026"))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        Judge judge = Judge.builder()
                .judgeNumber(request.getJudgeNumber())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .judgeType(request.getJudgeType())
                .status(request.getStatus())
                .user(user)
                .build();

        judgeRepository.save(judge);

        return mapToResponse(judge);

    }

    @Override
    public List<JudgeResponse> getAllJudges() {

        return judgeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    @Override
    public JudgeResponse getJudgeById(Long id) {

        return mapToResponse(findJudge(id));

    }

    @Override
    @Transactional
    public JudgeResponse updateJudge(Long id,
                                     JudgeRequest request) {

        Judge judge = findJudge(id);

        judge.setFullName(request.getFullName());
        judge.setPhone(request.getPhone());
        judge.setEmail(request.getEmail());
        judge.setJudgeType(request.getJudgeType());
        judge.setStatus(request.getStatus());

        judge.getUser().setFullName(request.getFullName());
        judge.getUser().setEmail(request.getEmail());

        judge.getUser().setRole(
                request.getJudgeType() == JudgeType.CHIEF
                        ? Role.ROLE_CHIEF_JUDGE
                        : Role.ROLE_JUDGE
        );

        judgeRepository.save(judge);

        return mapToResponse(judge);

    }

    @Override
    @Transactional
    public void deactivateJudge(Long id) {

        Judge judge = findJudge(id);

        judge.setStatus(JudgeStatus.INACTIVE);
        judge.getUser().setStatus(UserStatus.INACTIVE);

        judgeRepository.save(judge);

    }

    // ==========================
    // Helper Methods
    // ==========================

    private Judge findJudge(Long id) {

        return judgeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Judge not found."
                ));

    }

    private JudgeResponse mapToResponse(Judge judge) {

        return JudgeResponse.builder()
                .id(judge.getId())
                .judgeNumber(judge.getJudgeNumber())
                .fullName(judge.getFullName())
                .phone(judge.getPhone())
                .email(judge.getEmail())
                .judgeType(judge.getJudgeType())
                .status(judge.getStatus())
                .username(judge.getUser().getUsername())
                .build();

    }

}