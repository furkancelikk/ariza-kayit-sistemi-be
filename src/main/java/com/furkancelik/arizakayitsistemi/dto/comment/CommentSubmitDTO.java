package com.furkancelik.arizakayitsistemi.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentSubmitDTO {

    @NotNull
    @Size(max = 250, min = 3)
    private String context;

    @NotNull
    private Long postID;
}
