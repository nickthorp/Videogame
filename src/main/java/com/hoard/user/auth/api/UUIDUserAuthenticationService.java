package com.hoard.user.auth.api;

import com.hoard.user.crud.api.UserCrudService;
import com.hoard.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public class UUIDUserAuthenticationService implements UserAuthenticationService {

    @Autowired
    private UserCrudService users;

    @Override
    public Optional<String> login(final String username, final String password) {
        final String uuid = UUID.randomUUID().toString();
        final User user = User
                .builder()
                .id(uuid)
                .username(username)
                .password(password)
                .build();

        users.save(user);
        return Optional.of(uuid);
    }

    @Override
    public Optional<User> findByToken(final String token) {
        return users.find(token);
    }

    @Override
    public void logout(User user) {

    }
}
