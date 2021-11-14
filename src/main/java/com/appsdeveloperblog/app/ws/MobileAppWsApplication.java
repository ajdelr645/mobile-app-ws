package com.appsdeveloperblog.app.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.security.AppProperties;

/**
 * This is for the master and develop branch
 */
@SpringBootApplication
public class MobileAppWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}

	/**
	 * So you can use BCryptPasswordEncoder in UserServiceImpl. Since it is not a custom class
	 * with a @Component attribute
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * So SpringApplicationContext.getBean can be used in other classes
	 * 1. getting userServiceImpl in AuthenticationFilter. AuthenticationFilter cannot autowire
	 *    other beans
	 * 2. getting AppProperties in SecurityConstants. SecurityConstants cannot autowire
	 * Note: SpringApplicationContext is a custom class in this project
	 * 3. its method does not start with get because its not accessed using getBean
	 * @return
	 */
	@Bean 
	public SpringApplicationContext springApplicationContext()
	{
		return new SpringApplicationContext();
	}

	/**
	 * Used for getting AppProperties in SecurityConstants since SecurityConstants has no
	 * @Service or @Component annotation. It used SpringApplicationContext to get it.
	 * Note: SpringApplicationContext is a custom class in this project
	 * method starts with get because its accessed using getBean
	 * @return
	 */
	@Bean(name="AppProperties")
	public AppProperties getAppProperties()
	{
		return new AppProperties();
	}

}
