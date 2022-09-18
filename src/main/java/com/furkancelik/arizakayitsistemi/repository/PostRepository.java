package com.furkancelik.arizakayitsistemi.repository;

import com.furkancelik.arizakayitsistemi.model.Post;
import com.furkancelik.arizakayitsistemi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUser(Pageable pageable, User user);

//    Page<Post> findByIdLessThan(Long id, Pageable pageable);
//
//    Page<Post> findByUserAndIdLessThan(User user, Long id, Pageable pageable);

//    Long countByIdGreaterThan(Long id);
//
//    Long countByUserAndIdGreaterThan(User user, Long id);

//    List<Post> findByIdGreaterThan(Long id, Sort sort);
//
//    List<Post> findByUserAndIdGreaterThan(User user, Long id, Sort sort);
}
