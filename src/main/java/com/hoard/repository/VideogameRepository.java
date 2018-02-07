package com.hoard.repository;

import com.hoard.entity.Videogame;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideogameRepository extends CrudRepository<Videogame, Integer> {
    List<Videogame> findByUserId(Integer userId);
}