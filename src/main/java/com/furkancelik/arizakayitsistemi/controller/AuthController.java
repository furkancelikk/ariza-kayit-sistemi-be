package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.annotation.CurrentUser;
import com.furkancelik.arizakayitsistemi.dto.UserDTO;
import com.furkancelik.arizakayitsistemi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/1.0/auth")
public class AuthController {

    @PostMapping
    public ResponseEntity<?> handleAuthentication(@CurrentUser User user){
        return ResponseEntity.ok(new UserDTO(user));
    }
}
