package com.howtodoinjava.jersey;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

import com.howtodoinjava.jersey.provider.AuthenticationFilter;
import com.howtodoinjava.jersey.provider.GsonMessageBodyHandler;

public class CustomApplication extends ResourceConfig 
{
	public CustomApplication() 
	{
		packages("com.howtodoinjava.jersey");
		register(LoggingFilter.class);
		register(GsonMessageBodyHandler.class);
		register(AuthenticationFilter.class);
	}
}
