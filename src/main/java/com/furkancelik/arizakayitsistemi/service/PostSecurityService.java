package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.model.Post;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostSecurityService {

    @Autowired
    private PostRepository postRepository;

    public boolean isAllowedToDelete(Long id, User loggedUser){
        Optional<Post> postOptional = postRepository.findById(id);
        if (!postOptional.isPresent()){
            return false;
        }

        Post post = postOptional.get();

        return post.getUser().getId() == loggedUser.getId();
    }
}
