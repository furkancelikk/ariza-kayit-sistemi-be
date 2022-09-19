package com.furkancelik.arizakayitsistemi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    private String fileType;

    @OneToOne
    private Post post;
}
