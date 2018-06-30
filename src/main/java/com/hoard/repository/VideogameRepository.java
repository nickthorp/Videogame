package com.hoard.repository;

import com.hoard.videogame.entity.Videogame;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface VideogameRepository extends CrudRepository<Videogame, Integer> {
    Collection<Videogame> findByUserId(Integer userId);
}