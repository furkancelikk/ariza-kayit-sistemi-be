package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.dto.UpdateUserDTO;
import com.furkancelik.arizakayitsistemi.error.NotFoundException;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;

    public User save(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Page<User> getAll(Pageable pageable, User user) {
        if (user != null) {
            return userRepository.findByUsernameNot(user.getUsername(), pageable);
        }
        return userRepository.findAll(pageable);
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new NotFoundException("User not found");
        return user;
    }

    public User updateUser(String username, UpdateUserDTO updateUserDTO) {
        User user = findByUsername(username);
        user.setDisplayName(updateUserDTO.getDisplayName());
        if (updateUserDTO.getImage() != null) {
            String oldImage = user.getImage();
            try {
                String filename = fileService.base64ToFile(updateUserDTO.getImage());
                user.setImage(filename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileService.removeProfileImage(oldImage);
        }
        return userRepository.save(user);
    }
}
