package com.furkancelik.arizakayitsistemi.service;

import com.furkancelik.arizakayitsistemi.dto.category.CategorySubmitDTO;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.error.NotFoundException;
import com.furkancelik.arizakayitsistemi.model.Category;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category create(CategorySubmitDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    public Category findByID(Long id){
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent())
            throw new NotFoundException("Category doesnt exist");
        return categoryOptional.get();
    }

    public Page<Category> getAll(Pageable pageable, User user) {
        if (user.getRole() == UserRole.PERSONNEL){
            Specification specification = isUser(user.getUsername());
            return categoryRepository.findAll(specification, pageable );
        }
        return categoryRepository.findAll(pageable);
    }

    public List<Category> getNewCategories(Long id, String personnelName, Sort sort) {
        Specification specification = idGreaterThan(id);
        if (personnelName != null)
            specification = specification.and(isUser(personnelName));
        return categoryRepository.findAll(specification, sort);
    }

    public Page<Category> getOldCategories(Long id, String personnelName, Pageable pageable) {
        Specification specification = idLessThan(id);
        if (personnelName != null)
            specification = specification.and(isUser(personnelName));
        return categoryRepository.findAll(specification, pageable);
    }

    public void deleteByID(Long id) {
        Category category = this.findByID(id);
        categoryRepository.delete(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    Specification idGreaterThan(Long id){
        return ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get("id"), id);
        });
    }

    Specification idLessThan(Long id) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("id"), id);
        });
    }

    Specification isUser(String username){
        return ((root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            Root<User> userRoot = criteriaQuery.from(User.class);
            Expression<Collection<Category>> usersCategory = userRoot.get("categories");
            return criteriaBuilder.and(criteriaBuilder.equal(userRoot.get("username"), username), criteriaBuilder.isMember(root, usersCategory));
        });
    }

}
