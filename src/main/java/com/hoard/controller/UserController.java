package com.hoard.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.entity.User;
import com.hoard.repository.UserRepository;
import com.hoard.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/get/{id}")
    @JsonView(View.SummaryWithList.class)
    public ResponseEntity getUser(@PathVariable(value = "id") Integer id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "/create")
    public ResponseEntity create(@RequestBody User user) {
        if ( user.getId() != null ) { user.setId(null); }
        if ( validateUserEmail(user.getEmail()) ){
            // If username is blank, set to email
            if ( user.getUserName() == null || user.getUserName() == "" ) { user.setUserName(user.getEmail()); }
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("ID or Email already in use.", HttpStatus.BAD_REQUEST);
        }
    }

    //TODO Add JSON View
    @PutMapping(path = "/update/{id}")
    public ResponseEntity update(@PathVariable(value="id") Integer id, @RequestBody User user) {
        if (id == null) {
            return new ResponseEntity<>("Please provide a valid user ID.", HttpStatus.BAD_REQUEST);
        }
        if ( userRepository.findOne(id) == null ) {
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        if ( user.getEmail().isEmpty() || user.getEmail() == null ) {
            return new ResponseEntity<>("Please provide a valid email address.", HttpStatus.BAD_REQUEST);
        }
        if ( !validateUserEmail(user.getId(),user.getEmail()) ){
            return new ResponseEntity<>("Email already in use.", HttpStatus.BAD_REQUEST);
        }
        if ( user.getUserName().isEmpty() || user.getUserName() == null ) {
            return new ResponseEntity<>("Please provide a valid username.", HttpStatus.BAD_REQUEST);
        }
        User update = userRepository.findOne(id);
        update.setEmail(user.getEmail());
        update.setFirstName(user.getFirstName());
        update.setLastName(user.getLastName());
        update.setUserName(user.getUserName());
        return new ResponseEntity<>(userRepository.save(update), HttpStatus.OK);
    }

    //TODO Add JSON View
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity delete(@PathVariable(value="id") Integer id) {
        if (id == null) {
            return new ResponseEntity<>("Please provide a valid user ID.", HttpStatus.BAD_REQUEST);
        }
        if ( userRepository.findOne(id) == null ) {
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findOne(id);
        userRepository.delete(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private Boolean validateUserEmail(Integer id, String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty() || check.size() == 1 && check.get(0).getId().equals(id);
    }

    private Boolean validateUserEmail(String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty();
    }

    private Boolean validateUserId(Integer id) {
        User check = userRepository.findOne(id);
        return (check == null );
    }
}
