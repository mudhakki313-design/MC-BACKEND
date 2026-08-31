package com.qmcms.service;

import com.qmcms.entity.User;
import com.qmcms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    // ==========================================
    // FIND BY USERNAME
    // ==========================================

    @Override
    public Optional<User> findByUsername(String username) {

        return userRepository.findByUsername(username);

    }


    // ==========================================
    // SAVE
    // ==========================================

    @Override
    public User save(User user) {

        return userRepository.save(user);

    }


    // ==========================================
    // GET PROFILE
    // ==========================================

    @Override
    public Optional<User> getProfileByUsername(
            String username
    ) {

        return userRepository.findByUsername(username);

    }

}