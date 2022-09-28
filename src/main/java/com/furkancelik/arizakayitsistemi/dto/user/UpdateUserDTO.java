package com.furkancelik.arizakayitsistemi.dto.user;

import com.furkancelik.arizakayitsistemi.annotation.FileType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// kullanıcının güncelleyebileceği alanlar için oluşturulmuştur. modelde olduğu gibi validasyon şartları vardır
@Getter
@Setter
public class UpdateUserDTO {

    @NotNull(message = "{custom.constraints.displayName.NotNull.message}")
    @Size(min = 3, max = 254, message = "{custom.constraints.displayName.Size.message}")
    private String displayName;

    @FileType(types = {"image/png", "image/jpeg"})
    private String image;
}
