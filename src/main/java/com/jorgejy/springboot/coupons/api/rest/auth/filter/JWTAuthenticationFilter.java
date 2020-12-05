package com.jorgejy.springboot.coupons.api.rest.auth.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorgejy.springboot.coupons.api.rest.auth.service.JWTService;
import com.jorgejy.springboot.coupons.api.rest.auth.service.JWTServiceImplement;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTService jwtService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username != null && password != null) {
			logger.info("username from request form-data:  " + username);
			logger.info("password from request form-data:  " + password);

		} else {
			com.jorgejy.springboot.coupons.api.rest.models.User user = null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(),
						com.jorgejy.springboot.coupons.api.rest.models.User.class);
				username = user.getUsername();
				password = user.getPassword();

				logger.info("username from request raw:  " + username);
				logger.info("password from request raw:  " + password);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		username = username.trim();

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token = jwtService.create(authResult);

		response.addHeader(JWTServiceImplement.HEADER_STRING, JWTServiceImplement.TOKEN_PREFIX + token);

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("message", "Login successfull");

		logger.info("Token "+ token);
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");

	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		logger.info("unsuccessfulAuthentication");
		
		Map<String, Object> body = new HashMap<String, Object>();

		body.put("message", "Error login user o password invalid");
		body.put("error", failed.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");

	}
	
	
}
