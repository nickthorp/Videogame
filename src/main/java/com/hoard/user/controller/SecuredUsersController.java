package com.hoard.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.user.auth.api.UserAuthenticationService;
import com.hoard.user.entity.User;
import com.hoard.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
final class SecuredUsersController {

    @Autowired
    private UserAuthenticationService authentication;


    @GetMapping("/current")
    @JsonView(View.Summary.class)
    User getCurrent(@AuthenticationPrincipal final User user) {
        return user;
    }

    @GetMapping("/logout")
    boolean logout(@AuthenticationPrincipal final User user) {
        authentication.logout(user);
        return true;
    }
}
