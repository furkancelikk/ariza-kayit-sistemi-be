package com.furkancelik.arizakayitsistemi.repository;

import com.furkancelik.arizakayitsistemi.model.Post;
import com.furkancelik.arizakayitsistemi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUser(Pageable pageable, User user);

}
