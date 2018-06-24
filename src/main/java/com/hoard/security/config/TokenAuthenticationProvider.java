package com.hoard.security.config;

import com.hoard.user.auth.api.UserAuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
final public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    final private UserAuthenticationService auth;

    TokenAuthenticationProvider(UserAuthenticationService auth) {
        this.auth = auth;
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        final Object token = authentication.getCredentials();
        return Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(auth::findByToken)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with authentication token=" + token));
    }
}
