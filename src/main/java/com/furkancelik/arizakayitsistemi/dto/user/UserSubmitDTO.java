package com.furkancelik.arizakayitsistemi.dto.user;

import com.furkancelik.arizakayitsistemi.annotation.UniqueUsername;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserSubmitDTO {

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
}
