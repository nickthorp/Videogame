package com.hoard.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.entity.User;
import com.hoard.entity.Videogame;
import com.hoard.error.Errors;
import com.hoard.repository.UserRepository;
import com.hoard.repository.VideogameRepository;
import com.hoard.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/api/videogame")
public class VideogameController {
    @Autowired
    private VideogameRepository videogameRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/create/{userId}", produces = "application/json")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity create(@PathVariable(value="userId") Integer userId, @RequestBody Videogame videogame){
        if (userId == null) {return new ResponseEntity<>(Errors.INVALID_ID,HttpStatus.BAD_REQUEST);}
        User user = userRepository.findOne(userId);
        if (user == null) {return new ResponseEntity<>(Errors.ITEM_NOT_FOUND,HttpStatus.NOT_FOUND);}
        if ( videogame.getId() != null ) {
            return new ResponseEntity<>(Errors.JSON_EXTRA_FIELD, HttpStatus.BAD_REQUEST);
        }
        if (videogame != null && !videogame.getTitle().equals("") && videogame.getTitle() != null) {
            videogame.setUser(user);
            return new ResponseEntity<>(videogameRepository.save(videogame), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Errors.JSON_MALFORMED,HttpStatus.BAD_REQUEST);
        }
    }

    //TODO Create method to do a batch create?

    @GetMapping(path="/get/{id}", produces = "application/json")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity getVideogame (@PathVariable(value = "id") Integer id) {
        Videogame videogame = videogameRepository.findOne(id);
        if (videogame == null) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(videogame, HttpStatus.OK);
    }

    @GetMapping(path="/get/all/{userId}",produces = "application/json")
    @JsonView(View.Summary.class)
    public ResponseEntity getAllVideogames(@PathVariable(value = "userId") Integer userId) {
        if (userRepository.findOne(userId) == null){
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(videogameRepository.findByUserId(userId), HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}", produces = "application/json")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity update(@PathVariable(value ="id") Integer id,@RequestBody Videogame videogame) {
        if ( !id.equals(videogame.getId()) ) {
            return new ResponseEntity<>(Errors.INVALID_ID, HttpStatus.BAD_REQUEST);
        }
        if ( videogameRepository.findOne(id) == null ) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Videogame lookup = videogameRepository.findOne(id);
        if (lookup.equals(videogame)) {
            return new ResponseEntity<>(Errors.NO_CHANGE, HttpStatus.OK);
        }
        videogame.setUser(lookup.getUser());
        return new ResponseEntity<>(videogameRepository.save(videogame), HttpStatus.OK);
    }

    @DeleteMapping(path="/delete/{id}")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {
        if (videogameRepository.findOne(id) == null) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Videogame videogame = videogameRepository.findOne(id);
        videogameRepository.delete(videogame);
        if ( videogameRepository.findOne(id) == null ) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(Errors.ITEM_DELETE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}