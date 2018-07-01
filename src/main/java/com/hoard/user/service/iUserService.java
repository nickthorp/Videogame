package com.hoard.user.service;

import com.hoard.user.entity.User;
import org.springframework.http.ResponseEntity;

public interface iUserService {
    ResponseEntity create(User user);
    ResponseEntity read(Integer id);
    ResponseEntity update(User user, Integer id);
    ResponseEntity delete(Integer id);
}
