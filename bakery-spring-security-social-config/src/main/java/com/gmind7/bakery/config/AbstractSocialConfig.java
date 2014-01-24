package com.gmind7.bakery.config;

import javax.inject.Inject;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.config.annotation.EnableFacebook;
import org.springframework.social.facebook.web.DisconnectController;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.config.annotation.EnableTwitter;

//import com.gmind7.bakery.signin.SimpleSignInAdapter;

//@Configuration
@EnableTwitter(appId="${twitter.consumerKey}", appSecret="${twitter.consumerSecret}")
@EnableFacebook(appId="${facebook.clientId}", appSecret="${facebook.clientSecret}")
public abstract class AbstractSocialConfig {

    @Inject
	private Environment environment;
	
	@Inject 
	private ConnectionRepository connectionRepository;
	
	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;
	
	public ConnectionFactoryLocator getConnectionFactoryLocator(){
    	return this.connectionFactoryLocator;
    }
	
	@Bean
	@Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES) 
	public UsersConnectionRepository usersConnectionRepository() {
		return new JdbcUsersConnectionRepository(getDataSouce(), connectionFactoryLocator, Encryptors.noOpText());
	}
	
	@Bean
	public UserIdSource userIdSource() {
		return new AuthenticationNameUserIdSource();
	}
    
    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }
    
    @Bean
	public DisconnectController disconnectController() {
		return new DisconnectController(usersConnectionRepository(), environment.getProperty("facebook.clientSecret"));
	}
    
    public abstract DataSource getDataSouce();
    
//  @Bean
//	public ProviderSignInController providerSignInController(RequestCache requestCache) {
//		return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository(), new SimpleSignInAdapter(requestCache));
//	}
    
    @Bean
	public ConnectController connectController() {
		ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
		connectController = addInterceptor(connectController);
		//connectController.addInterceptor(new PostToWallAfterConnectInterceptor());
		//connectController.addInterceptor(new TweetAfterConnectInterceptor());
		return connectController;
	}
    
    public abstract ConnectController addInterceptor(ConnectController connectController);
    
}
