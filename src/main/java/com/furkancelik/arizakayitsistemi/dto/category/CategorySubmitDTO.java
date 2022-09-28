package com.furkancelik.arizakayitsistemi.dto.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategorySubmitDTO {

    @NotNull
    @Size(min = 3, max = 50)
    private String name;
}
