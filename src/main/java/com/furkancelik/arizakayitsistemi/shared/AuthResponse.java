package com.furkancelik.arizakayitsistemi.shared;

import com.furkancelik.arizakayitsistemi.dto.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String token;
    private UserDTO user;
}
