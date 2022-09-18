package com.furkancelik.arizakayitsistemi.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class PostSubmitDTO {

    @Size(min = 5, max = 1000, message = "{custom.constraints.post.content.Size.message}")
    private String content;

    private Long attachmentID;
}
