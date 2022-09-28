package com.furkancelik.arizakayitsistemi.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.furkancelik.arizakayitsistemi.dto.category.CategoryDTO;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

// User dönülecek yerlerde tercih edilir
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private String username;
    private String displayName;
    private String image;
    private UserRole role;

//    private List<CategoryDTO> categories;

    // user içerisinden gerekli alanlar ile oluşturulan constructor
    public UserDTO(User user) {
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.image = user.getImage();
        this.role = user.getRole();
//        if (user.getRole() == UserRole.PERSONNEL && user.getCategories().size() > 0)
//            this.categories = user.getCategories().stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
}
