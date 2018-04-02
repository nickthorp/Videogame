package com.hoard.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.entity.User;
import com.hoard.entity.Videogame;
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

    @GetMapping(path="/get/{id}")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity getVideogame (@PathVariable(value = "id") Integer id) {
        Videogame videogame = videogameRepository.findOne(id);
        if (videogame == null) {
            return new ResponseEntity<>("Videogame not found.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(videogame, HttpStatus.OK);
    }

    @GetMapping(path="/get/all/{userId}")
    @JsonView(View.Summary.class)
    public ResponseEntity getAllVideogames(@PathVariable(value = "userId") Integer userId) {
        if (userRepository.findOne(userId) == null){
            return new ResponseEntity<>("User not found.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(videogameRepository.findByUserId(userId), HttpStatus.OK);
    }

    @PostMapping(path="/create/{userId}")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity create(@PathVariable(value="userId") Integer userId, @RequestBody Videogame videogame){
        if (userId == null) {return new ResponseEntity<>("No user ID provided.",HttpStatus.BAD_REQUEST);}
        User user = userRepository.findOne(userId);
        if (user == null) {return new ResponseEntity<>("User not found.",HttpStatus.BAD_REQUEST);}
        if (videogame != null && videogame.getTitle() != null) {
            videogame.setUser(user);
            return new ResponseEntity<>(videogameRepository.save(videogame), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Bad videogame!",HttpStatus.BAD_REQUEST);
        }
    }

    //TODO Create method to do a batch create

    @PutMapping(path = "/update/{id}")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity update(@PathVariable(value ="id") Integer id,@RequestBody Videogame videogame) {
        Videogame lookup = videogameRepository.findOne(id);
        if ( lookup == null ) {
            return new ResponseEntity<>("Videogame not found.", HttpStatus.BAD_REQUEST);
        }
        if ( !id.equals(videogame.getId()) ) {
            return new ResponseEntity<>("Invalid id. Ensure id in JSON body matches id in HTTP request", HttpStatus.BAD_REQUEST);
        }
        if (lookup.equals(videogame)) {
            return new ResponseEntity<>("No change.", HttpStatus.OK);
        }
        videogame.setUser(lookup.getUser());
        return new ResponseEntity<>(videogameRepository.save(videogame), HttpStatus.OK);
    }

    @DeleteMapping(path="/delete/{id}")
    @JsonView(View.SummaryWithUser.class)
    public ResponseEntity delete(@PathVariable(value = "id") Integer id) {
        Videogame videogame = videogameRepository.findOne(id);
        if (videogame == null) {
            return new ResponseEntity<>("Videogame not found", HttpStatus.BAD_REQUEST);
        }
        videogameRepository.delete(videogame);
        if ( videogameRepository.findOne(id) == null ) {
            return new ResponseEntity<>("Item successfully deleted.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Error deleting item.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}