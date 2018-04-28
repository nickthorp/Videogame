package com.hoard.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.entity.User;
import com.hoard.error.Errors;
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

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity create(@RequestBody User user) {
        if ( user.getId() != null ) {
            return new ResponseEntity<>(Errors.JSON_EXTRA_FIELD, HttpStatus.BAD_REQUEST);
        }
        if ( validateUserEmail(user.getEmail()) ){
            if ( user.getUserName() == null || user.getUserName().equals("") ) { user.setUserName(user.getEmail()); }
                User result = userRepository.save(user);
                if ( result != null ){
                    return new ResponseEntity<>(result, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(Errors.ITEM_CREATE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

        } else {
            return new ResponseEntity<>(Errors.ITEM_IN_USE, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(path = "/get/{id}", produces = "application/json")
    @JsonView(View.SummaryWithList.class)
    public ResponseEntity getUser(@PathVariable(value = "id") Integer id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}", produces = "application/json")
    @JsonView(View.Summary.class)
    public ResponseEntity update(@PathVariable(value="id") Integer id, @RequestBody User user) {
        if ( !id.equals(user.getId())) {
            return new ResponseEntity<>(Errors.INVALID_ID, HttpStatus.BAD_REQUEST);
        }
        if ( userRepository.findOne(id) == null ) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User lookup = userRepository.findOne(id);
        if ( user.getEmail().isEmpty() || user.getEmail() == null ) {
            return new ResponseEntity<>(Errors.INVALID_EMAIL, HttpStatus.BAD_REQUEST);
        }
        if ( !validateUserEmail(user.getId(),user.getEmail()) ){
            return new ResponseEntity<>(Errors.ITEM_IN_USE, HttpStatus.BAD_REQUEST);
        }
        if ( user.getUserName().isEmpty() || user.getUserName() == null ) {
            return new ResponseEntity<>(Errors.INVALID_USERNAME, HttpStatus.BAD_REQUEST);
        }
        if (lookup.equals(user)) {
            return new ResponseEntity<>(Errors.NO_CHANGE, HttpStatus.OK);
        }
        lookup.setEmail(user.getEmail());
        lookup.setFirstName(user.getFirstName());
        lookup.setLastName(user.getLastName());
        lookup.setUserName(user.getUserName());
        return new ResponseEntity<>(userRepository.save(lookup), HttpStatus.OK);
    }

    //TODO Add deleting of all user's items?
    @DeleteMapping(path = "/delete/{id}")
    @JsonView(View.Summary.class)
    public ResponseEntity delete(@PathVariable(value="id") Integer id) {
        if ( userRepository.findOne(id) == null ) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findOne(id);
        userRepository.delete(user);
        if ( userRepository.findOne(id) == null ){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(Errors.ITEM_DELETE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Boolean validateUserEmail(Integer id, String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty() || check.size() == 1 && check.get(0).getId().equals(id);
    }

    private Boolean validateUserEmail(String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty();
    }
}
