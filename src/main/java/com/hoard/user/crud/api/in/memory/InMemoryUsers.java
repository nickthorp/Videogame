package com.hoard.user.crud.api.in.memory;

import com.hoard.entity.User;
import com.hoard.user.crud.api.UserCrudService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
final class InMemoryUsers implements UserCrudService {

    private Map<String, User> users = new HashMap<>();

    @Override
    public User save(final User user) {
        return users.put(user.getId().toString(), user);
    }

    @Override
    public Optional<User> find(final String id) {
        return ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        return users
                .values()
                .stream()
                .filter(u -> Objects.equals(username, u.getUsername()))
                .findFirst();
    }
}
