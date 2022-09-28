package com.furkancelik.arizakayitsistemi.dto.post;

import com.furkancelik.arizakayitsistemi.model.Category;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PostSubmitDTO {

    @Size(min = 5, max = 1000, message = "{custom.constraints.post.content.Size.message}")
    private String content;

    private Long attachmentID;

    @NotNull(message = "En az bir kategori seçiniz")
    private Long categoryID;
}
