package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.dto.post.PostDTO;
import com.furkancelik.arizakayitsistemi.dto.post.PostSubmitDTO;
import com.furkancelik.arizakayitsistemi.dto.post.PostUpdateDTO;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.error.NotFoundException;
import com.furkancelik.arizakayitsistemi.model.Category;
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

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FileAttachmentRepository fileAttachmentRepository;
    private final FileService fileService;
    private final CategoryService categoryService;

    public void createPost(@Valid PostSubmitDTO postSubmitDTO, User user) {

        Category category = categoryService.findByID(postSubmitDTO.getCategoryID());

        Post post = new Post();

        post.setContent(postSubmitDTO.getContent());
        post.setUser(user);
        post.setCategory(category);
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

    public Page<Post> getAll(Pageable pageable, User user, Long categoryID) {
        Specification specification = null;
        if (user.getRole() == UserRole.PERSONNEL){
            specification = isPersonnel(user);
        }
        if (categoryID != null){
            Category category = categoryService.findByID(categoryID);
            specification = (specification == null) ? isCategory(category) : specification.and(isCategory(category));
        }
        return specification == null ? postRepository.findAll(pageable) : postRepository.findAll(specification, pageable);
    }

    public Page<Post> getByUser(Pageable pageable, User user, User currentUser, Long categoryID) {
        Specification specification = isUser(user);
        if (categoryID != null){
            Category category = categoryService.findByID(categoryID);
            specification = specification.and(isCategory(category));
        }
        if (currentUser.getRole() == UserRole.PERSONNEL)
            specification = specification.and(isPersonnel(currentUser));
        return postRepository.findAll(specification, pageable);
    }

    public Page<Post> getOldPosts(Long id, String username, User currentUser, Long categoryID, Pageable pageable) {
        Specification<Post> specification = idLessThan(id);
        if (username != null) {
            User user = userService.findByUsername(username);
            specification = specification.and(isUser(user));
        }
        if (categoryID != null){
            Category category = categoryService.findByID(categoryID);
            specification = specification.and(isCategory(category));
        }
        if (currentUser.getRole() == UserRole.PERSONNEL)
            specification = specification.and(isPersonnel(currentUser));
        return postRepository.findAll(specification, pageable);
    }

    public Long getNewPostsCount(Long id, String username, User currentUser, Long categoryID) {
        Specification<Post> specification = idGreaterThan(id);
        if (username != null) {
            User user = userService.findByUsername(username);
            specification = specification.and(isUser(user));
        }
        if (categoryID != null){
            Category category = categoryService.findByID(categoryID);
            specification = specification.and(isCategory(category));
        }
        if (currentUser.getRole() == UserRole.PERSONNEL)
            specification = specification.and(isPersonnel(currentUser));
        return postRepository.count(specification);
    }

    public List<Post> getNewPosts(Long id, String username, User currentUser, Long categoryID, Sort sort) {
        Specification<Post> specification = idGreaterThan(id);
        if (username != null) {
            User user = userService.findByUsername(username);
            specification = specification.and(isUser(user));
        }
        if (categoryID != null){
            Category category = categoryService.findByID(categoryID);
            specification = specification.and(isCategory(category));
        }
        if (currentUser.getRole() == UserRole.PERSONNEL)
            specification.and(isPersonnel(currentUser));
        return postRepository.findAll(specification, sort);
    }

    public void deleteByID(Long id) {
        Post post = postRepository.getOne(id);
        if (post.getAttachment() != null){
            String fileName = post.getAttachment().getName();
            fileService.removeAttachment(fileName);
        }
        postRepository.deleteById(id);
    }

    public Post findByID(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (!postOptional.isPresent())
            throw new NotFoundException("Post not found");
        return postOptional.get();
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

    Specification isCategory(Category category) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("category"), category);
        });
    }

    Specification isPersonnel(User user) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);

            Root<User> userRoot = criteriaQuery.from(User.class);
            Expression<Collection<Category>> usersCategory = userRoot.get("categories");

            return criteriaBuilder.and(criteriaBuilder.equal(userRoot.get("id"), user.getId()), criteriaBuilder.isMember(root.get("category"), usersCategory));
        });
    }

    public Post updatePost(PostUpdateDTO postUpdateDTO) {
        Post post = this.findByID(postUpdateDTO.getId());
        post.setIsResolved(postUpdateDTO.getIsResolved());
        return postRepository.save(post);
    }
}
