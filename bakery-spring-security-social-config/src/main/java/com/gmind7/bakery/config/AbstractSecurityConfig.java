package com.gmind7.bakery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUserDetailsService;

import com.gmind7.bakery.user.SocialUserDetailsServiceImpl;
import com.gmind7.bakery.user.UserDetailsServiceImpl;
import com.gmind7.bakery.user.UserRepository;

//@Configuration
@EnableWebSecurity
@ImportResource("classpath:spring/auditing-context.xml")
public abstract class AbstractSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public abstract void configure(WebSecurity web) throws Exception;
//    {
//        web.ignoring().antMatchers("/resources/**");
//    }

    @Override
    protected abstract void configure(HttpSecurity http) throws Exception;
//    {
//        http
//                //Configures form signin
//                .formLogin()
//                    .loginPage("/signin")
//                    .loginProcessingUrl("/signin/authenticate")
//                    .failureUrl("/signin?error=bad_credentials")
//                //Configures the logout function
//                .and()
//                    .logout()
//                        .deleteCookies("JSESSIONID")
//                        .logoutUrl("/signout")
//                        .logoutSuccessUrl("/")
//                //Configures url based authorization
//                .and()
//                    .authorizeRequests()
//                        //Anyone can access the urls
//                        .antMatchers(
//                                "/auth/**",
//                                "/login",
//                                "/signin/**",
//                                "/signup/**",
//                                "/user/**"
//                        ).permitAll()
//                        //The rest of the our application is protected.
//                        .antMatchers("/**").hasRole("USER")
//                //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
//                .and()
//                    .apply(new SpringSocialConfigurer());
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
        return new SocialUserDetailsServiceImpl(userDetailsService());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }
    
}
