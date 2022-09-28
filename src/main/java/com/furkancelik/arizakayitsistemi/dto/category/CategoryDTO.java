package com.furkancelik.arizakayitsistemi.dto.category;

import com.furkancelik.arizakayitsistemi.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    private Long id;
    private String name;
    private Integer postCount;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.postCount = category.getPosts().size();
    }
}
