package com.jorgejy.springboot.coupons.api.rest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgejy.springboot.coupons.api.rest.daos.UserDao;
import com.jorgejy.springboot.coupons.api.rest.models.User;

@Service
public class UserServiceImplements implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return ((List<User>) userDao.findAll()).stream()
			.map(user -> {
				user.setPassword("");
				return user;
			}).collect(Collectors.toList());
	}

	@Override
	public User findUserById(Long id) {
		User  user = userDao.findById(id).orElse(null);
		if(user != null)
			user.setPassword("");
		return user;
	}

}
