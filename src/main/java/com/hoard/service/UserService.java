package com.hoard.service;

import com.hoard.entity.User;
import com.hoard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private User user;
    private Integer id;

    @Autowired
    private UserRepository userRepository;

    public UserService(){}

    public UserService(User user, Integer id){
        this.user = user;
        this.id = id;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setId(Integer id){
        this.id = id;
    }


}
