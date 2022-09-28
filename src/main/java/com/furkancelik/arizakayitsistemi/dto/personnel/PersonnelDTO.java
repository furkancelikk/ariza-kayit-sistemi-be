package com.furkancelik.arizakayitsistemi.dto.personnel;

import com.furkancelik.arizakayitsistemi.dto.category.CategoryDTO;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PersonnelDTO {

    private String username;
    private String displayName;
    private String image;
    private UserRole role;
    private List<CategoryDTO> categories;

    public PersonnelDTO(User user) {
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.image = user.getImage();
        this.role = user.getRole();
        this.categories = user.getCategories().stream().map(CategoryDTO::new).collect(Collectors.toList());
    }
}
