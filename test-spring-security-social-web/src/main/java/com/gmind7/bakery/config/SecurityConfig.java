package com.gmind7.bakery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
public class SecurityConfig extends AbstractSecurityConfig {

	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            //Configures form signin
            .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/signin/authenticate")
                .failureUrl("/signin?error=bad_credentials")
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
                .apply(new SpringSocialConfigurer());
    }

}
