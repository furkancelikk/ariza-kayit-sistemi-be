package com.furkancelik.arizakayitsistemi.repository;

import com.furkancelik.arizakayitsistemi.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

    List<Token> findByCreationBefore(Date date);
}
