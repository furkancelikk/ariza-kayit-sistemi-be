package com.furkancelik.arizakayitsistemi.dto.comment;

import com.furkancelik.arizakayitsistemi.dto.post.PostDTO;
import com.furkancelik.arizakayitsistemi.dto.user.UserDTO;
import com.furkancelik.arizakayitsistemi.model.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String context;
    private Date date;
    private UserDTO user;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.context = comment.getContext();
        this.date = comment.getCreationTime();
        this.user = new UserDTO(comment.getUser());
    }
}
