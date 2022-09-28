package com.furkancelik.arizakayitsistemi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
// opaque
public class Token {

    @Id
    private String token;

    @ManyToOne
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;
}
