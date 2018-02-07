package com.hoard.controller;

import com.hoard.entity.Videogame;
import com.hoard.repository.VideogameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/api")
public class VideogameController {
    @Autowired
    private VideogameRepository videogameRepository;

    @GetMapping(path="/videogame")
    public @ResponseBody Videogame getVideogame (@RequestParam Integer id) {
        return videogameRepository.findOne(id);
    }

    @GetMapping(path="/videogame/all")
    public @ResponseBody Iterable<Videogame> getAllVideogames(@RequestParam Integer userId) {
        return videogameRepository.findByUserId(userId);
    }

    @PostMapping(path="/post")
    public ResponseEntity<Videogame> update(@RequestBody Videogame videogame){
        if (videogame != null) {
            if (videogame.getuserId() != null && videogame.getTitle() !=null) {
                videogameRepository.save(videogame);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<Videogame>(videogame, HttpStatus.OK);
    }

    @DeleteMapping(path="/videogame")
    public ResponseEntity<Videogame> delete(@RequestParam Integer id) {
        Videogame videogame = videogameRepository.findOne(id);
        if (videogame == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        videogameRepository.delete(videogame);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}