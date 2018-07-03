package com.hoard.user.controller;

import com.hoard.user.auth.api.UserAuthenticationService;
import com.hoard.user.entity.User;
import com.hoard.user.service.iUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public/users")
final class PublicUserController {

    @Autowired
    private UserAuthenticationService authentication;
    @Autowired
    private iUserService users;

    @PostMapping(path = "/register")
    String register(@RequestBody final User user) {
        ResponseEntity response = users.create(user);
        if(response.getStatusCode() != HttpStatus.CREATED) {
            return "Oops! Something went wrong!";
        }
        return login(user.getEmail(), user.getPassword());
    }

    @PostMapping(path = "/login")
    String login(
            @RequestParam("email") final String email,
            @RequestParam("password") final String password) {
        return authentication
                .login(email, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }
}
