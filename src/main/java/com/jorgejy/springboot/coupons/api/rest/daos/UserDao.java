package com.jorgejy.springboot.coupons.api.rest.daos;

import org.springframework.data.repository.CrudRepository;

import com.jorgejy.springboot.coupons.api.rest.models.User;


public interface UserDao extends CrudRepository<User, Long>{
	
	public User findByUsername(String username);
	
}
