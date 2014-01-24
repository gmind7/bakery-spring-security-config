package com.gmind7.bakery.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialUserDetailsService;

import com.gmind7.bakery.security.AuthenticationUserIdExtractor;
import com.gmind7.bakery.security.SimpleSocialUsersDetailService;

@Configuration
public class SocialSecurityConfig {

    @Inject
    private UsersConnectionRepository usersConnectionRepository;

    @Bean
    public SocialAuthenticationFilter socialAuthenticationFilter(AuthenticationManager authenticationManager, SocialAuthenticationServiceLocator authenticationServiceLocator) {
        SocialAuthenticationFilter socialAuthenticationFilter = new SocialAuthenticationFilter(authenticationManager, userIdSource(), usersConnectionRepository, authenticationServiceLocator);
        socialAuthenticationFilter.setSignupUrl("/signup"); // TODO: Fix filter to handle in-app paths
// socialAuthenticationFilter.setRememberMeServices(rememberMeServices);
        return socialAuthenticationFilter;
    }

    @Bean
    public SocialAuthenticationProvider socialAuthenticationProvider(UserDetailsService userDetailsService) {
        return new SocialAuthenticationProvider(usersConnectionRepository, socialUsersDetailsService(userDetailsService));
    }

    @Bean
    public SocialUserDetailsService socialUsersDetailsService(UserDetailsService userDetailsService) {
        return new SimpleSocialUsersDetailService(userDetailsService);
    }

    @Bean
    public UserIdSource userIdSource() {
        return new AuthenticationUserIdExtractor();
    }

}

