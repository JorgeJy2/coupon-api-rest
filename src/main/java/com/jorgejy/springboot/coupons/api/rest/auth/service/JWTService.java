package com.jorgejy.springboot.coupons.api.rest.auth.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import io.jsonwebtoken.Claims;

public interface JWTService {

	public String create(Authentication authentication) throws IOException ;

	public boolean validate(String token);

	public Claims getClaims(String token);

	public String getUserName(String token);

	public Collection<? extends GrantedAuthority> getAuthorities(String token) throws IOException ;

	public String resolve(String token);
}
