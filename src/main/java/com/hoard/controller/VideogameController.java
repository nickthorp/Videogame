package com.hoard.controller;

import com.hoard.entity.Videogame;
import com.hoard.repository.VideogameRepository;
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

    @GetMapping(path="/get")
    public @ResponseBody Videogame getVideogame (@RequestParam Integer id) {
        return videogameRepository.findOne(id);
    }

    @GetMapping(path="/get/all")
    public @ResponseBody Iterable<Videogame> getAllVideogames(@RequestParam Integer userId) {
        return videogameRepository.findByUserId(userId);
    }

    @PostMapping(path="/create")
    public ResponseEntity<Videogame> create(@RequestBody Videogame videogame){
        if (videogame != null) {
            if (videogame.getUser() != null && videogame.getTitle() !=null) {
                videogameRepository.save(videogame);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Videogame>(videogame, HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity update(@RequestBody Videogame videogame) {
        if ( videogame.getId() == null ) {
            return new ResponseEntity<>("Please provide a valid ID.", HttpStatus.BAD_REQUEST);
        }
        if ( videogameRepository.findOne(videogame.getId()) == null ) {
            return new ResponseEntity<>("Videogame not found.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(videogameRepository.save(videogame), HttpStatus.OK);
    }

    @DeleteMapping(path="/delete")
    public ResponseEntity<Videogame> delete(@RequestParam Integer id) {
        Videogame videogame = videogameRepository.findOne(id);
        if (videogame == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        videogameRepository.delete(videogame);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}