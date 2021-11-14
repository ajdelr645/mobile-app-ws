package com.appsdeveloperblog.app.ws.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;

/**
 * Defined URL requests that need authorization
 * Defined filters
 */
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true) //to enable @Secured and @PreAuthorize
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	/**
	 * This class extends Spring's UserDetailsService class
	 */
	private final UserService userDetailsService;

	/**
	 * This class implements Spring's PasswordEncoder
	 */
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * 1. Both classes above needs to implements/extend the needed classes to be able to be used in
	 * void configure(AuthenticationManagerBuilder auth) method
	 * 2. UserServiceImpl implements loadUserByUsername method of UserDetailsService class (of spring) because UserService extends it. This
	 *    method is called during login
	 * 3. BCryptPasswordEncoder implements encode and matches method PasswordEncoder, the class it implements. Matches method
	 *    will automatically be called upon login
	 */


    private final UserRepository userRepository;

    public WebSecurity(UserService userDetailsService,
    		BCryptPasswordEncoder bCryptPasswordEncoder,
    		UserRepository userRepository
    		) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .cors().and()
        .csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
        .permitAll()
        .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
        .permitAll()
        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
        .permitAll()
        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
        .permitAll()
        .antMatchers(SecurityConstants.H2_CONSOLE)
        .permitAll()
        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
        .permitAll()
        //.antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")  //commented due to @EnableGlobalMethodSecurity
        .anyRequest().authenticated().and()
        .addFilter(getAuthenticationFilter())
        .addFilter(new AuthorizationFilter(authenticationManager(), userRepository))
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.headers().frameOptions().disable();
    }

	/**
	 * Processes will be done by spring in the back end . All we need to do is tell spring which class we use for userdetails and
	 * for password encoding.
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	
    protected AuthenticationFilter getAuthenticationFilter() throws Exception {
	    final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
	    filter.setFilterProcessesUrl("/users/login");
	    return filter;
	}
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
    	final CorsConfiguration configuration = new CorsConfiguration();
    	   
    	configuration.setAllowedOrigins(Arrays.asList("*"));
    	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
    	configuration.setAllowCredentials(true);
    	configuration.setAllowedHeaders(Arrays.asList("*"));
    	
    	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	
    	return source;
    }

}
