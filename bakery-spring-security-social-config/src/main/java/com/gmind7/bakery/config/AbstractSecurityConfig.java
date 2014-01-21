package com.gmind7.bakery.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
    private ConfigurableApplicationContext context;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }
	
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SocialUserDetailsService socialUserDetailsService() {
        return new SocialUserDetailsServiceImpl(userDetailsService());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    // 이 부분 적용 필요
//    @Override
//    protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select username, password, true from Account where username = ?")
//                .authoritiesByUsernameQuery("select username, 'ROLE_USER' from Account where username = ?")
//                .and()
//            .authenticationProvider(socialAuthenticationProvider);
//    }
    
    
    
    
    @Override
    public abstract void configure(WebSecurity web) throws Exception;
//    {
//        web.ignoring().antMatchers("/resources/**");
//    }

    @Override
    protected abstract void configure(HttpSecurity http) throws Exception;
//	{   http
//	    //Configures form signin
//	    .formLogin()
//	        .loginPage("/signin")
//	        .loginProcessingUrl("/signin/authenticate")
//	        .failureUrl("/signin?error=bad_credentials")
//	        .usernameParameter("j_username")
//	        .passwordParameter("j_password")
//	    //Configures the logout function
//	    .and()
//	        .logout()
//	            .deleteCookies("JSESSIONID")
//	            .logoutUrl("/signout")
//	            .logoutSuccessUrl("/")
//	    //Configures url based authorization
//	    .and()
//	        .authorizeRequests()
//	            //Anyone can access the urls
//	            .antMatchers(
//	            	    "/favicon.ico",
//	            	    "/resources/**", 
//	                    "/auth/**",
//	                    "/login",
//	                    "/signin/**",
//	                    "/signup/**",
//	                    "/user/**",
//	                    "/disconnect/**"
//	            ).permitAll()
//	            //The rest of the our application is protected.
//	            .antMatchers("/**").hasRole("USER")
//	    //Adds the SocialAuthenticationFilter to Spring Security's filter chain.
//	    .and()
//	        .addFilterBefore(socialAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
//	}
    
}
