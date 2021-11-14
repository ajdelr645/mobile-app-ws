package com.appsdeveloperblog.app.ws.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repository.UserRepository;
import io.jsonwebtoken.Jwts;

/**
 * This class because it extends BasicAuthenticationFilter, it Processes a HTTP request's BASIC
 * authorization headers, putting the result into the SecurityContextHolder.
 * For more read about:
 * https://www.loginradius.com/blog/async/everything-you-want-to-know-about-authorization-headers/
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {
	
	private final UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository) {
        super(authManager);
        this.userRepository = userRepository;
     }

    /**
     * Validates the Authorization Field in the Header of the request
     * This method is called on every request that needs to be authorized
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(SecurityConstants.HEADER_STRING);
		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        
        if (token != null) {
            
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
            
            String user = Jwts.parser()
                    .setSigningKey( SecurityConstants.getTokenSecret() )
                    .parseClaimsJws( token )
                    .getBody()
                    .getSubject();
            
            if (user != null) {
            	UserEntity userEntity = userRepository.findByEmail(user);
            	if(userEntity == null) return null;
            	
            	UserPrincipal userPrincipal = new UserPrincipal(userEntity);
                return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
            }
            
            return null;
        }
        
        return null;
    }

}
