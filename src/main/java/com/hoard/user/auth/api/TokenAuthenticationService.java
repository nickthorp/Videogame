package com.hoard.user.auth.api;

import com.google.common.collect.ImmutableMap;
import com.hoard.user.entity.User;
import com.hoard.user.repository.UserRepository;
import com.hoard.user.token.api.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TokenAuthenticationService implements UserAuthenticationService {

    @Autowired
    private TokenService tokens;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<String> login(String email, String password) {
        return userRepository
                .findByEmail(email)
                .filter(user -> Objects.equals(password, user.getPassword()))
                .map(user -> tokens.expiring(ImmutableMap.of("email", email)));

    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional
                .of(tokens.verify(token))
                .map(map -> map.get("email"))
                .flatMap(userRepository::findByEmail);
    }

    @Override
    public void logout(User user) {

    }
}
