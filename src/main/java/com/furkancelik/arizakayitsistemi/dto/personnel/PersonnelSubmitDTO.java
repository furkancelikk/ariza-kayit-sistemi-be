package com.furkancelik.arizakayitsistemi.dto.personnel;

import com.furkancelik.arizakayitsistemi.annotation.FileType;
import com.furkancelik.arizakayitsistemi.annotation.OneOf;
import com.furkancelik.arizakayitsistemi.annotation.UniqueUsername;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.model.Category;
import com.furkancelik.arizakayitsistemi.model.Post;
import com.furkancelik.arizakayitsistemi.model.Token;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PersonnelSubmitDTO {

    @NotNull(message = "{custom.constraints.username.NotNull.message}")//(message = "Username can not be null")
    @Size(min = 3, max = 254, message = "{custom.constraints.username.Size.message}")
    @UniqueUsername
    private String username;

    @NotNull(message = "{custom.constraints.displayName.NotNull.message}")
    @Size(min = 3, max = 254, message = "{custom.constraints.displayName.Size.message}")
    private String displayName;

    @NotNull(message = "{custom.constraints.password.NotNull.message}")
    //Pattern eklemeyi unutma
    //@Pattern(regexp = "")
    private String password;

    @ManyToMany(mappedBy = "users")
    @NotEmpty(message = "Personelin ilgileneceği en az bir kategori seçiniz")
    private List<Category> categories;
}
