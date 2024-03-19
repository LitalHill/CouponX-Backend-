package com.authforge.service;

import com.authforge.web.dto.UserDto;

import jakarta.security.auth.message.AuthException;

public interface AuthForgeService {
    String authenticateAndGenerateToken(String username, String password) throws AuthException;

    UserDto parseTokenAndGetUser(String token);

    boolean signUp(String username, String password);
}
