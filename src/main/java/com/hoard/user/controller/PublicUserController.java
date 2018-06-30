package com.hoard.user.controller;

import com.hoard.error.Errors;
import com.hoard.user.auth.api.UserAuthenticationService;
import com.hoard.user.crud.api.UserCrudService;
import com.hoard.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/users")
final class PublicUserController {
    private UserAuthenticationService authentication;
    private UserCrudService users;

    @PostMapping("/register")
    String register(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        //users.save( new User(username, password) );

        return login(username, password);
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
