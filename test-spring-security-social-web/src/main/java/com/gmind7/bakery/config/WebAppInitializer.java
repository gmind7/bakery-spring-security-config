package com.gmind7.bakery.config;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.gmind7.bakery.config.AbstractWebAppInitializer;

public class WebAppInitializer extends AbstractWebAppInitializer {

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return new javax.servlet.Filter[] { encodingFilter, 
			                                new HiddenHttpMethodFilter(), 
			                                new ShallowEtagHeaderFilter() };
	}

}
