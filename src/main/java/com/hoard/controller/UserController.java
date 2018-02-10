package com.hoard.controller;

import com.hoard.entity.User;
import com.hoard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path="/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/get")
    public @ResponseBody User getUser (@RequestParam Integer id) {
        return userRepository.findOne(id);
    }

    @PostMapping(path="/post")
    public ResponseEntity update(@Valid @RequestBody User user){
        User result;
        if (user != null) {
            List<User> check = userRepository.findByEmail(user.getEmail());
            if ( !check.isEmpty() && user.getId() != null && user.getId().equals( check.get(0).getId()) ) {
                result = userRepository.save(user);
            } else if ( check.isEmpty() ) {
                result = userRepository.save(user);
            } else {
                return new ResponseEntity<>("User with that email already exists.", HttpStatus.OK);
            }
        } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path="/delete")
    public ResponseEntity<User> delete(@RequestParam Integer id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
