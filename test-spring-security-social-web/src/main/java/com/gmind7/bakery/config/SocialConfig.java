package com.gmind7.bakery.config;

import javax.inject.Inject;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

@Configuration
public class SocialConfig extends AbstractSocialConfig {

	@Inject
	private DataSourceConfig dataSouceConfig;
	
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(
        		dataSouceConfig.defaultDataSource(),
                connectionFactoryLocator,
                /**
                 * The TextEncryptor object encrypts the authorization details of the connection. In
                 * our example, the authorization details are stored as plain text.
                 * DO NOT USE THIS IN PRODUCTION.
                 */
                Encryptors.noOpText()
        );
    }

}
