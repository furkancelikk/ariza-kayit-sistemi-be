package com.furkancelik.arizakayitsistemi.dto.post;

import com.furkancelik.arizakayitsistemi.dto.FileAttachmentDTO;
import com.furkancelik.arizakayitsistemi.dto.user.UserDTO;
import com.furkancelik.arizakayitsistemi.model.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {

    private Long id;

    private String content;

    private long timestamp;

    private UserDTO user;

    private FileAttachmentDTO attachment;

    private Boolean isResolved;

    public PostDTO(Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.timestamp = post.getTimestamp().getTime();
        this.user = new UserDTO(post.getUser());
        this.isResolved = post.getIsResolved();
        if (post.getAttachment() != null)
            this.attachment = new FileAttachmentDTO(post.getAttachment());
    }
}
