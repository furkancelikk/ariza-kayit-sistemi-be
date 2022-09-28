package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.dto.CredentialsDTO;
import com.furkancelik.arizakayitsistemi.dto.user.UserDTO;
import com.furkancelik.arizakayitsistemi.error.AuthException;
import com.furkancelik.arizakayitsistemi.model.FileAttachment;
import com.furkancelik.arizakayitsistemi.model.Token;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.TokenRepository;
import com.furkancelik.arizakayitsistemi.repository.UserRepository;
import com.furkancelik.arizakayitsistemi.shared.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableScheduling
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
        tokenEntity.setCreation(new Date());
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

    public boolean isRoleAllowed(User loggedUser, String[] roles){
        for (String role : roles) {
            if (loggedUser.getRole().toString().equals(role))
                return true;
        }
        throw new AuthException();
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanExpiredToken() {
        Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)); // 24 saatin milisaniye cinsi
        List<Token> tokenList = tokenRepository.findByCreationBefore(twentyFourHoursAgo);
        for (Token token : tokenList) {
            this.clearToken(token.getToken());
        }
    }
}
