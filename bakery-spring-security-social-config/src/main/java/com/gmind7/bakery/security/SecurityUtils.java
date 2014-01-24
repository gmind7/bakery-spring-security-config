package com.gmind7.bakery.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gmind7.bakery.user.User;
import com.gmind7.bakery.user.UserDetails;

public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    public static void logInUser(User user) {
    	log.info("Logging in user: {}", user);

        UserDetails userDetails = UserDetails.getBuilder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(user.getRole())
                .socialSignInProvider(user.getSignInProvider())
                .username(user.getEmail())
                .build();
        log.debug("Logging in principal: {}", userDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User: {} has been logged in.", userDetails);
    }
}
