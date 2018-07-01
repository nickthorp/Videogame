package com.hoard.user.service;

import com.hoard.user.entity.User;
import com.hoard.error.Errors;
import com.hoard.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
final class UserService implements iUserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){}

    private Boolean validateUserEmail(Integer id, String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty() || check.size() == 1 && check.get(0).getId().equals(id);
    }

    private Boolean validateUserEmail(String email) {
        List<User> check = userRepository.findByEmail(email);
        return check.isEmpty();
    }

    @Override
    public ResponseEntity create(User user){
        if ( user.getId() != null ) {
            return new ResponseEntity<>(Errors.JSON_EXTRA_FIELD, HttpStatus.BAD_REQUEST);
        }
        if ( validateUserEmail(user.getEmail()) ){
            if ( user.getUsername() == null || user.getUsername().equals("") ) { user.setUsername(user.getEmail()); }
            User result = userRepository.save(user);
            if ( userRepository.findById(result.getId()).isPresent() ){
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(Errors.ITEM_CREATE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<>(Errors.ITEM_IN_USE, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity read(Integer id) {
        if ( !userRepository.findById(id).isPresent() ) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(User user, Integer id) {
        if ( !id.equals(user.getId())) {
            return new ResponseEntity<>(Errors.INVALID_ID, HttpStatus.BAD_REQUEST);
        }
        if ( user.getUsername() == null || user.getUsername().isEmpty() ) {
            return new ResponseEntity<>(Errors.INVALID_USERNAME, HttpStatus.BAD_REQUEST);
        }
        if ( !userRepository.findById(id).isPresent() ) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User lookup = userRepository.findById(id).get();
        if ( user.getEmail().isEmpty() || user.getEmail() == null ) {
            return new ResponseEntity<>(Errors.INVALID_EMAIL, HttpStatus.BAD_REQUEST);
        }
        if ( !validateUserEmail(user.getId(),user.getEmail()) ){
            return new ResponseEntity<>(Errors.ITEM_IN_USE, HttpStatus.BAD_REQUEST);
        }
        if (lookup.equals(user)) {
            return new ResponseEntity<>(Errors.NO_CHANGE, HttpStatus.OK);
        }
        lookup.setEmail(user.getEmail());
        lookup.setFirstName(user.getFirstName());
        lookup.setLastName(user.getLastName());
        lookup.setUsername(user.getUsername());
        return new ResponseEntity<>(userRepository.save(lookup), HttpStatus.OK);
    }

    @Override
    public ResponseEntity delete(Integer id) {
        if ( !userRepository.findById(id).isPresent() ) {
            return new ResponseEntity<>(Errors.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
        if ( !userRepository.findById(id).isPresent() ){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(Errors.ITEM_DELETE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
