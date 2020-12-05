package com.jorgejy.springboot.coupons.api.rest.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jorgejy.springboot.coupons.api.rest.models.User;
import com.jorgejy.springboot.coupons.api.rest.models.response.ResponseServiceModelMap;
import com.jorgejy.springboot.coupons.api.rest.services.UserService;

@RestController
@RequestMapping("user-api")
@Secured("ROLE_ADMIN")
public class UserController {

	private static Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping()
	public ResponseEntity<Map<String, Object>> getAll() {

		ResponseServiceModelMap<List<User>> modelResponse;
		ResponseEntity<Map<String, Object>> response;
		HttpStatus httpStatus;

		try {
			modelResponse = new ResponseServiceModelMap<List<User>>("User found.", "", userService.getAllUsers());
			httpStatus = HttpStatus.OK;
		} catch (DataAccessException e) {
			modelResponse = new ResponseServiceModelMap<List<User>>("Error get all coupons in database.",e.getMessage(), null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse = new ResponseServiceModelMap<List<User>>("Unknown error.", e.getMessage(), null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} 
		
		response = new ResponseEntity<Map<String, Object>>(modelResponse.getModelResponse(), httpStatus);
		log.info(response.toString());	
		
		return response;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
		ResponseServiceModelMap<User> modelResponse = new ResponseServiceModelMap<User>();
		ResponseEntity<Map<String, Object>> response;
		HttpStatus httpStatus;

		try {
			User user = userService.findUserById(id);
			if (user != null) {
				modelResponse = new ResponseServiceModelMap<User>("User found.", "", userService.findUserById(id));
				httpStatus = HttpStatus.OK;
			} else {
				modelResponse = new ResponseServiceModelMap<User>("User not found.", "user is empty.", null);
				httpStatus = HttpStatus.NOT_FOUND;
			}

		} catch (DataAccessException e) {
			modelResponse = new ResponseServiceModelMap<User>("Error search in database.", e.getMessage(), null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse = new ResponseServiceModelMap<User>("Unknown error.", e.getMessage(), null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} 

		response = new ResponseEntity<Map<String, Object>>(modelResponse.getModelResponse(), httpStatus);
		log.info(response.toString());	
		
		return response;

	}

}
