package com.qmcms.service;

import com.qmcms.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User save(User user);

    Optional<User> getProfileByUsername(String username);

}