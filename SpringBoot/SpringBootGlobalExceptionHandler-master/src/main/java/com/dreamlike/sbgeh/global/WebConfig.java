/**
 * 
 */
package com.dreamlike.sbgeh.global;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Broly
 *
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return registration;
	}

}
