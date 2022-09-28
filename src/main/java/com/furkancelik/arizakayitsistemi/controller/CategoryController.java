package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.annotation.CurrentUser;
import com.furkancelik.arizakayitsistemi.dto.category.CategoryDTO;
import com.furkancelik.arizakayitsistemi.dto.category.CategorySubmitDTO;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.service.CategoryService;
import com.furkancelik.arizakayitsistemi.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/1.0/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("@authService.isRoleAllowed(principal, {'ADMIN'})")
    public CategoryDTO create(@Valid @RequestBody CategorySubmitDTO category){
        return new CategoryDTO(categoryService.create(category));
    }

    @GetMapping
    @PreAuthorize("@authService.isRoleAllowed(principal, {'ADMIN', 'USER', 'PERSONNEL'})")
    public ResponseEntity<?> getAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                    @RequestParam(name = "all", required = false, defaultValue = "false") boolean allList,
                                    @CurrentUser User currentUser){
        if (allList)
            return ResponseEntity.ok(categoryService.findAll().stream().map(CategoryDTO::new));
        return ResponseEntity.ok(categoryService.getAll(pageable, currentUser).map(CategoryDTO::new));
    }


    @DeleteMapping(value = "/{id:[0-9]+}")
    @PreAuthorize("@authService.isRoleAllowed(principal, {'ADMIN'})")
    public GenericResponse deleteByID(@PathVariable("id") Long id){
        categoryService.deleteByID(id);
        return new GenericResponse("Category deleted");
    }

    @GetMapping(value = "/{id:[0-9]+}") // id değerinin sayı olduğunu belirtmek regular exp kullanıldı. + işareti     //birden fazla basamak için kullanıldı
    @PreAuthorize("@authService.isRoleAllowed(principal, {'ADMIN', 'PERSONNEL'})")
    public ResponseEntity<?> getAllRelative(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                            @PathVariable(name = "id") Long id,
                                            @RequestParam(name = "direction", defaultValue = "before", required = false) String direction,
                                            @RequestParam(name = "personnel", required = false) String personnelName) {
        if (direction.equals("after")) {
            List<CategoryDTO> newPosts = categoryService.getNewCategories(id, personnelName, pageable.getSort()).stream().
                    map(CategoryDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(newPosts);
        }
        return ResponseEntity.ok(categoryService.getOldCategories(id, personnelName, pageable).map(CategoryDTO::new));
    }

}
