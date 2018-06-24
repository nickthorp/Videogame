package com.hoard.controller;

import com.hoard.error.Errors;
import com.hoard.user.auth.api.UserAuthenticationService;
import com.hoard.user.crud.api.UserCrudService;
import com.hoard.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/users")
final class PublicUserController {
    private UserAuthenticationService authentication;
    private UserCrudService users;

    private Boolean validateUserEmail(Integer id, String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty() || check.size() == 1 && check.get(0).getId().equals(id);
    }

    private Boolean validateUserEmail(String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty();
    }

    @PostMapping("/register")
    String register(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        users.save( new User(username, password) );

        return login(username, password);
    }

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity create(@RequestBody User user) {
        if ( user.getId() != null ) {
            return new ResponseEntity<>(Errors.JSON_EXTRA_FIELD, HttpStatus.BAD_REQUEST);
        }
        if ( validateUserEmail(user.getEmail()) ){
            if ( user.getUsername() == null || user.getUsername().equals("") ) { user.setUsername(user.getEmail()); }
            User result = userRepository.save(user);
            if ( userRepository.findById(result.getId()).isPresent() ){
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(Errors.ITEM_CREATE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<>(Errors.ITEM_IN_USE, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/login")
    String login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        return authentication
                .login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }
}
