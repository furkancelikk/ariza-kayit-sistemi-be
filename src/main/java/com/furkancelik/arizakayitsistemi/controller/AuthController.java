package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.dto.CredentialsDTO;
import com.furkancelik.arizakayitsistemi.service.AuthService;
import com.furkancelik.arizakayitsistemi.shared.AuthResponse;
import com.furkancelik.arizakayitsistemi.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/api/1.0/auth")
    public AuthResponse handleAuthentication(@RequestBody CredentialsDTO credentials) {
        return authService.authenticate(credentials);
    }

    @PostMapping(value = "/api/1.0/logout")
    public GenericResponse handleLogout(@RequestHeader(name = "AUTHORIZATION") String authorization) {
        String token = authorization.substring(7);
        authService.clearToken(token);
        return new GenericResponse("Logout success");
    }
}
