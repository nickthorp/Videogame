package com.hoard.service;

import com.hoard.user.entity.User;
import org.springframework.http.ResponseEntity;

public interface iUserService {
    ResponseEntity create(User user);

}
