package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.annotation.CurrentUser;
import com.furkancelik.arizakayitsistemi.dto.comment.CommentDTO;
import com.furkancelik.arizakayitsistemi.dto.comment.CommentSubmitDTO;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/1.0/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public CommentDTO save(@Valid @RequestBody CommentSubmitDTO commentSubmitDTO, @CurrentUser User user){
        return new CommentDTO(commentService.create(commentSubmitDTO, user));
    }

    @GetMapping(value = "/post/{id:[0-9]+}")
    public List<CommentDTO> getPostComment(@PathVariable("id") Long postID){
        return commentService.getPostComment(postID).stream().map(CommentDTO::new).collect(Collectors.toList());
    }
}
