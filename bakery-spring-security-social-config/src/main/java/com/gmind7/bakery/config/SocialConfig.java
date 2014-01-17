package com.gmind7.bakery.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableJdbcConnectionRepository;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.config.annotation.EnableFacebook;
import org.springframework.social.facebook.web.DisconnectController;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.config.annotation.EnableTwitter;

import com.gmind7.bakery.social.facebook.PostToWallAfterConnectInterceptor;
import com.gmind7.bakery.social.twitter.TweetAfterConnectInterceptor;

@Configuration
@Profile("simple")
@EnableJdbcConnectionRepository
@EnableTwitter(appId="${twitter.consumerKey}", appSecret="${twitter.consumerSecret}")
@EnableFacebook(appId="${facebook.clientId}", appSecret="${facebook.clientSecret}")
public class SocialConfig {

	@Inject
	private Environment environment;
	
	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;
	
	@Inject 
	private ConnectionRepository connectionRepository;

	@Inject 
	private UsersConnectionRepository usersConnectionRepository;

	@Bean
	public ConnectController connectController() {
		ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
		connectController.addInterceptor(new PostToWallAfterConnectInterceptor());
		connectController.addInterceptor(new TweetAfterConnectInterceptor());
		return connectController;
	}
	
	@Bean
	public DisconnectController disconnectController() {
		return new DisconnectController(usersConnectionRepository, environment.getProperty("facebook.clientSecret"));
	}
	
	@Bean
	public UserIdSource userIdSource() {
		return new AuthenticationNameUserIdSource();
	}

}