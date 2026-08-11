package com.qmcms.service;

import com.qmcms.dto.request.LoginRequest;
import com.qmcms.dto.response.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);

}