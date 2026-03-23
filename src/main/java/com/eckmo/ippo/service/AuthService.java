package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.request.LoginRequest;
import com.eckmo.ippo.domain.dto.request.RegisterRequest;
import com.eckmo.ippo.domain.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    JwtResponse register(RegisterRequest request);
}
