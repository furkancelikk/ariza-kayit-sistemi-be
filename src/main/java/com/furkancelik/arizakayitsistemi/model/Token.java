package com.furkancelik.arizakayitsistemi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
// opaque
public class Token {

    @Id
    private String token;

    @ManyToOne
    private User user;
}
