package com.furkancelik.arizakayitsistemi.dto;

import com.furkancelik.arizakayitsistemi.model.User;
import lombok.Data;

// User dönülecek yerlerde tercih edilir
@Data
public class UserDTO {

    private String username;
    private String displayName;
    private String image;

    // user içerisinden gerekli alanlar ile oluşturulan constructor
    public UserDTO(User user) {
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.image = user.getImage();
    }
}
