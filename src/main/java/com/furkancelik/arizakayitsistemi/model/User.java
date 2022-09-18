package com.furkancelik.arizakayitsistemi.model;

import com.furkancelik.arizakayitsistemi.annotation.FileType;
import com.furkancelik.arizakayitsistemi.annotation.UniqueUsername;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Data
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

//    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("Role_user");
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
