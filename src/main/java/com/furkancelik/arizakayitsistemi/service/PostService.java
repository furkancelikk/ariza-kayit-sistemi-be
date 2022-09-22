package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.dto.PostDTO;
import com.furkancelik.arizakayitsistemi.dto.PostSubmitDTO;
import com.furkancelik.arizakayitsistemi.model.FileAttachment;
import com.furkancelik.arizakayitsistemi.model.Post;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.FileAttachmentRepository;
import com.furkancelik.arizakayitsistemi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FileAttachmentRepository fileAttachmentRepository;
    private final FileService fileService;

    public void createPost(@Valid PostSubmitDTO postSubmitDTO, User user) {
        Post post = new Post();

        post.setContent(postSubmitDTO.getContent());
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        if (postSubmitDTO.getAttachmentID() != null) {
            Optional<FileAttachment> attachmentOptional = fileAttachmentRepository.findById(postSubmitDTO.getAttachmentID());
            if (attachmentOptional.isPresent()) {
                FileAttachment attachment = attachmentOptional.get();
                attachment.setPost(savedPost);
                fileAttachmentRepository.save(attachment);
            }
        }
    }

    public Page<Post> getAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Page<Post> getByUser(Pageable pageable, User user) {
        return postRepository.findByUser(pageable, user);
    }

    public Page<Post> getOldPosts(Long id, String username, Pageable pageable) {
        Specification<Post> specification = idLessThan(id);
        if (username != null) {
            User user = userService.findByUsername(username);
            specification = specification.and(isUser(user));
        }
        return postRepository.findAll(specification, pageable);
    }

    public Long getNewPostsCount(Long id, String username) {
        Specification<Post> specification = idGreaterThan(id);
        if (username != null) {
            User user = userService.findByUsername(username);
            specification = specification.and(isUser(user));
        }
        return postRepository.count(specification);
    }

    public List<Post> getNewPosts(Long id, String username, Sort sort) {
        Specification<Post> specification = idGreaterThan(id);
        if (username != null) {
            User user = userService.findByUsername(username);
            specification = specification.and(isUser(user));
        }
        return postRepository.findAll(specification, sort);
    }

    Specification<Post> idGreaterThan(Long id) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get("id"), id);
        });
    }

    Specification<Post> idLessThan(Long id) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("id"), id);
        });
    }

    Specification<Post> isUser(User user) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("user"), user);
        });
    }

    public void deleteByID(Long id) {
        Post post = postRepository.getOne(id);
        if (post.getAttachment() != null){
            String fileName = post.getAttachment().getName();
            fileService.removeAttachment(fileName);
        }
        postRepository.deleteById(id);
    }

    public PostDTO findByID(Long id) {
        return new PostDTO(postRepository.findById(id).get());
    }
}
