package com.gmind7.bakery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationProvider;

@Configuration
public class SecurityConfig extends AbstractSecurityConfig {

	@Autowired
    private SocialAuthenticationProvider socialAuthenticationProvider;

    @Autowired
    private SocialAuthenticationFilter socialAuthenticationFilter;
    
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            //Configures form signin
            .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/signin/authenticate")
                .failureUrl("/signin?error=bad_credentials")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
            //Configures the logout function
            .and()
                .logout()
                    .deleteCookies("JSESSIONID")
                    .logoutUrl("/signout")
                    .logoutSuccessUrl("/")
            //Configures url based authorization
            .and()
                .authorizeRequests()
                    //Anyone can access the urls
                    .antMatchers(
                    	    "/favicon.ico",
                    	    "/resources/**", 
                            "/auth/**",
                            "/login",
                            "/signin/**",
                            "/signup/**",
                            "/user/**",
                            "/disconnect/**"
                    ).permitAll()
                    //The rest of the our application is protected.
                    .antMatchers("/**").hasRole("USER")
            //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
            .and()
                .addFilterBefore(socialAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }

}
