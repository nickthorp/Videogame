package com.hoard.user.crud.api;

import com.hoard.entity.User;

import java.util.Optional;

public interface UserCrudService {
    User save(User user);

    Optional<User> find(String id);

    Optional<User> findByUsername(String username);
}
