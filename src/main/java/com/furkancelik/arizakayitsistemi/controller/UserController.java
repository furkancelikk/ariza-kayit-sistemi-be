package com.furkancelik.arizakayitsistemi.controller;

import com.furkancelik.arizakayitsistemi.annotation.CurrentUser;
import com.furkancelik.arizakayitsistemi.dto.user.UpdateUserDTO;
import com.furkancelik.arizakayitsistemi.dto.user.UserDTO;
import com.furkancelik.arizakayitsistemi.dto.user.UserSubmitDTO;
import com.furkancelik.arizakayitsistemi.enums.UserRole;
import com.furkancelik.arizakayitsistemi.error.ApiError;
import com.furkancelik.arizakayitsistemi.model.User;
import com.furkancelik.arizakayitsistemi.service.UserService;
import com.furkancelik.arizakayitsistemi.shared.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/1.0/user")
@RequiredArgsConstructor
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping
    @PreAuthorize("@authService.isRoleAllowed(principal, {'ADMIN'})")
    public GenericResponse createUser(@Valid @RequestBody UserSubmitDTO userSubmitDTO){
        logger.info(userSubmitDTO.toString());
        User user = new User(userSubmitDTO);
        userService.save(user, UserRole.USER);
        return new GenericResponse("User created");
    }

    @GetMapping
    public Page<UserDTO> getAll(Pageable pageable, @CurrentUser User user){
        return userService.getAll(pageable, user).map(UserDTO::new);
    }

    @GetMapping(value = "/{username}")
    public UserDTO getByUsername(@PathVariable("username") String username){
        return new UserDTO(userService.findByUsername(username));
    }

    @PutMapping(value = "/{username}")
    @PreAuthorize("#username == principal.username")  // # parametre için
    public UserDTO updateUser(@PathVariable("username") String username, @Valid @RequestBody UpdateUserDTO updateUserDTO){
        return new UserDTO(userService.updateUser(username, updateUserDTO));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(MethodArgumentNotValidException notValidException, HttpServletRequest request){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), "Validation Error");

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError: notValidException.getBindingResult().getFieldErrors()){
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        apiError.setValidationErrors(validationErrors);
        return apiError;
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("#username == principal.username")
    public GenericResponse deleteByUsername(@PathVariable("username") String username){
        userService.deleteByUsername(username);
        return new GenericResponse("Deleted");
    }
}
