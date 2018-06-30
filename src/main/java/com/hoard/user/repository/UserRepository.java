package com.hoard.user.repository;

import com.hoard.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByEmail(String email);

}