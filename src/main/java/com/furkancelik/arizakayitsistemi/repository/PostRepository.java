package com.furkancelik.arizakayitsistemi.repository;

import com.furkancelik.arizakayitsistemi.model.Post;
import com.furkancelik.arizakayitsistemi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUser(Pageable pageable, User user);

    @Query(nativeQuery = true, value = "SELECT * from post p " +
            "join category c on p.category_id = c.id " +
            "join user_categories uc on uc.categories_id = c.id " +
            "join user u on u.id = uc.users_id " +
            "where u.id = :userID")
    List<Post> findPersonelCategoriesPost(Long userID);
}
