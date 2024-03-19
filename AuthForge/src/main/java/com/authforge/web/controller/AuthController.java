package com.authforge.web.controller;

import com.authforge.service.AuthForgeService;
import com.authforge.web.dto.LoginRequest;
import com.authforge.web.dto.SignUpRequest;
import com.authforge.web.dto.UserDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthForgeService authForgeService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        if (authForgeService.signUp(signUpRequest.getUsername(), signUpRequest.getPassword())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest)
            throws AuthException {
        String token = authForgeService.authenticateAndGenerateToken(loginRequest.getUsername(),
                                                                     loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/parse-token")
    public ResponseEntity<UserDto> parseToken(@RequestHeader("Authorization") String token) {
        UserDto userDto = authForgeService.parseTokenAndGetUser(token);
        return ResponseEntity.ok(userDto);
    }
}
