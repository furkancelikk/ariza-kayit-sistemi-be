package com.furkancelik.arizakayitsistemi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 1000, message = "{custom.constraints.post.content.Size.message}")
    @Column(length = 1000)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @ManyToOne
    private User user;

    // orphanRemoval post silindiğinde onunla ilişkili olanı da siler
    // Cascade.REMOVE da kullanılabilir bu da post silindiğinde attachment siler
    @OneToOne(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    private FileAttachment attachment;
}
