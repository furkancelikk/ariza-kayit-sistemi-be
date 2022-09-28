package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.dto.personnel.PersonnelDTO;
import com.furkancelik.arizakayitsistemi.dto.personnel.PersonnelSubmitDTO;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/1.0/personnels")
public class PersonnelController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("@authService.isRoleAllowed(principal, {'ADMIN'})")
    public PersonnelDTO createPersonnel(@Valid @RequestBody PersonnelSubmitDTO personnelSubmitDTO){
        User user = new User(personnelSubmitDTO);
        User personnel = userService.save(user, UserRole.PERSONNEL);
        return new PersonnelDTO(personnel);
    }
}
