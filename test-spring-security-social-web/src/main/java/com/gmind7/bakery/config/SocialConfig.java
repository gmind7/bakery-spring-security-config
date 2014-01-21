package com.gmind7.bakery.config;

import javax.inject.Inject;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.web.ConnectController;


@Configuration
public class SocialConfig extends AbstractSocialConfig {

	@Inject
	private DataSourceConfig dataSouceConfig;

	@Override
	public DataSource getDataSouce() {
		return dataSouceConfig.defaultDataSource();
	}

	@Override
	public ConnectController addInterceptor(ConnectController connectController) {
		return connectController;
	}

}
