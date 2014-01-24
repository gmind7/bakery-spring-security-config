package com.gmind7.bakery.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SocialUserDetailsServiceImpl.class);

    private UserDetailsService userDetailsService;

    public SocialUserDetailsServiceImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
    	log.debug("Loading user by user id: {}", userId);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        log.debug("Found user details: {}", userDetails);

        return (SocialUserDetails) userDetails;
    }
}
