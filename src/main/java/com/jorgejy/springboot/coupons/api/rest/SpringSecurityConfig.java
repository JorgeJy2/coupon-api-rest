package com.jorgejy.springboot.coupons.api.rest;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jorgejy.springboot.coupons.api.rest.auth.filter.JWTAuthenticationFilter;
import com.jorgejy.springboot.coupons.api.rest.auth.filter.JWTAuthorizationFilter;
import com.jorgejy.springboot.coupons.api.rest.auth.handler.LoginSuccessHandler;
import com.jorgejy.springboot.coupons.api.rest.auth.service.JWTService;
import com.jorgejy.springboot.coupons.api.rest.services.JpaUserDetailsService;

 
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoderDI;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private JpaUserDetailsService jpaUserDetailsService; 

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
		/*
		 * PasswordEncoder passwordEncoder = passwordEncoderDI; UserBuilder userBuilder
		 * = User.builder().passwordEncoder(passwordEncoder::encode);
		 * 
		 * authenticationManagerBuilder.inMemoryAuthentication()
		 * .withUser(userBuilder.username("admin").password("12345").roles("ADMIN",
		 * "USER"))
		 * .withUser(userBuilder.username("jorge").password("12345").roles("USER"));
		 */	
		
		
		/*
		 * authenticationManagerBuilder.jdbcAuthentication() .dataSource(dataSource)
		 * .passwordEncoder(passwordEncoderDI)
		 * .usersByUsernameQuery("select username, password, enabled from users where username=?"
		 * )
		 * .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?"
		 * );
		 */
		
		
		authenticationManagerBuilder.userDetailsService(jpaUserDetailsService).passwordEncoder(passwordEncoderDI);
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/","/css/**", "/js/**", "/images/**", "/list/**", "/locale/**").permitAll()
		.anyRequest().authenticated()
		.and().addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtService))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(),jwtService))
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
}