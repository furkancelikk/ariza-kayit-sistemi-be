package com.furkancelik.arizakayitsistemi.model;

import com.furkancelik.arizakayitsistemi.annotation.FileType;
import com.furkancelik.arizakayitsistemi.annotation.OneOf;
import com.furkancelik.arizakayitsistemi.annotation.UniqueUsername;
import com.furkancelik.arizakayitsistemi.dto.personnel.PersonnelSubmitDTO;
import com.furkancelik.arizakayitsistemi.dto.user.UserSubmitDTO;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Lob
    @FileType(types = {"image/png", "image/jpeg"})
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Token> token = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role alani bos olamaz")
    @OneOf(values = {"ADMIN", "PERSONNEL", "USER"})
    private UserRole role;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Category> categories = new ArrayList<>();

    public User(PersonnelSubmitDTO personnelSubmitDTO) {
        this.username = personnelSubmitDTO.getUsername();
        this.displayName = personnelSubmitDTO.getDisplayName();
        this.password = personnelSubmitDTO.getPassword();
        this.categories = personnelSubmitDTO.getCategories();
    }

    public User() {}

    public User(UserSubmitDTO userSubmitDTO) {
        this.username = userSubmitDTO.getUsername();
        this.displayName = userSubmitDTO.getDisplayName();
        this.password = userSubmitDTO.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
