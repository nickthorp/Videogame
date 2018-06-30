package com.hoard.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.user.entity.User;
import com.hoard.user.service.iUserService;
import com.hoard.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private iUserService userService;

    @PostMapping(produces = "application/json")
    public ResponseEntity create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @JsonView(View.SummaryWithList.class)
    public ResponseEntity getUser(@PathVariable(value = "id") Integer id) {
        return userService.read(id);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @JsonView(View.Summary.class)
    public ResponseEntity update(@PathVariable(value="id") Integer id, @RequestBody User user) {
        return userService.update(user, id);
    }

    //TODO Add deleting of all user's items?

    @DeleteMapping(path = "/{id}")
    @JsonView(View.Summary.class)
    public ResponseEntity delete(@PathVariable(value="id") Integer id) {
        return userService.delete(id);
    }
}
