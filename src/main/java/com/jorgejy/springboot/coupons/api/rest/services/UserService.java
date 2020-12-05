package com.jorgejy.springboot.coupons.api.rest.services;

import java.util.List;

import com.jorgejy.springboot.coupons.api.rest.models.User;

public interface UserService {

	public List<User> getAllUsers();
	public User findUserById(Long id); 
}
