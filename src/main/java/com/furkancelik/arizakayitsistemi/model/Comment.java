package com.furkancelik.arizakayitsistemi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //yorumun oluşturulma zamanını tutar
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;


    //yorumun içeriğini belirtir
    @Column(name = "CONTEXT", nullable = false, columnDefinition = "TEXT")
    private String context;


    //Bir Kullanıcı birden fazla yorum oluşturabilir
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;

    //Bir Başlıkta Birden fazla yorum olabilir yorumun hangi başlık altında olduğunu söyler
    @ManyToOne
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(referencedColumnName = "id")
    private Post post;
}
