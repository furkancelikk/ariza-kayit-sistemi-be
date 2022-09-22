package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.dto.CredentialsDTO;
import com.furkancelik.arizakayitsistemi.dto.UserDTO;
import com.furkancelik.arizakayitsistemi.error.AuthException;
import com.furkancelik.arizakayitsistemi.model.Token;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.TokenRepository;
import com.furkancelik.arizakayitsistemi.repository.UserRepository;
import com.furkancelik.arizakayitsistemi.shared.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    public AuthResponse authenticate(CredentialsDTO credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());
        if (user == null)
            throw new AuthException("Username or password incorrect");

        BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = cryptPasswordEncoder.matches(credentials.getPassword(), user.getPassword());

        if (!matches)
            throw new AuthException("Username or password incorrect");

        UserDTO userDTO = new UserDTO(user);
        String token = generateRandomToken();

        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setUser(user);
        tokenRepository.save(tokenEntity);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setUser(userDTO);
        authResponse.setToken(token);
        return authResponse;
    }

    public UserDetails getUserDetails(String token) {

        Optional<Token> tokenOptional = tokenRepository.findById(token);
        if (!tokenOptional.isPresent())
            return null;

        return tokenOptional.get().getUser();
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString().replaceAll("#", "");
    }

    public void clearToken(String token) {
        tokenRepository.deleteById(token);
    }
}
