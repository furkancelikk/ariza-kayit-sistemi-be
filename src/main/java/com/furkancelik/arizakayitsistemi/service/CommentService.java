package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.dto.comment.CommentDTO;
import com.furkancelik.arizakayitsistemi.dto.comment.CommentSubmitDTO;
import com.furkancelik.arizakayitsistemi.model.Category;
import com.furkancelik.arizakayitsistemi.model.Comment;
import com.furkancelik.arizakayitsistemi.model.Post;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public Comment create(CommentSubmitDTO commentSubmitDTO, User user) {
        Post post = postService.findByID(commentSubmitDTO.getPostID());
        Comment comment = new Comment();
        comment.setContext(commentSubmitDTO.getContext());
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreationTime(new Date());
        return commentRepository.save(comment);
    }

    public List<Comment> getPostComment(Long postID) {
        Post post = postService.findByID(postID);
        Specification specification = isPost(post);
        return commentRepository.findAll(specification, Sort.by("creationTime").descending());
    }

    Specification isPost(Post post) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("post"), post);
        });
    }
}
