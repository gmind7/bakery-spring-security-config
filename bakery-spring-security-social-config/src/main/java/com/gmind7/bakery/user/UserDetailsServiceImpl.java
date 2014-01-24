package com.gmind7.bakery.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
    	log.debug("Loading user by username: {}", username);

        User user = repository.findByEmail(username);
        log.debug("Found user: {}", user);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        UserDetails principal = UserDetails.getBuilder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(user.getRole())
                .socialSignInProvider(user.getSignInProvider())
                .username(user.getEmail())
                .build();

        log.debug("Returning user details: {}", principal);

        return principal;
    }
}
