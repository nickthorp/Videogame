package com.hoard.user.controller;

import com.hoard.user.auth.api.UserAuthenticationService;
import com.hoard.user.entity.User;
import com.hoard.user.service.iUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/public/users")
final class PublicUserController {

    //private UserAuthenticationService authentication;
    private iUserService users;

    @PostMapping(path = "/register")
    String register(@RequestBody final User user) {
        ResponseEntity response = users.create(user);
        if(response.getStatusCode() != HttpStatus.CREATED) {
            return "Oops! Something went wrong!";
        }
        //return login(user.getUsername(), user.getPassword());
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
