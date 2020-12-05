package com.jorgejy.springboot.coupons.api.rest.controllers;

import java.util.HashMap;
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

import com.jorgejy.springboot.coupons.api.rest.models.ResponseService;
import com.jorgejy.springboot.coupons.api.rest.models.User;
import com.jorgejy.springboot.coupons.api.rest.services.UserService;



@RestController
@RequestMapping("user-api")
public class UserController {
	
	private static Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping()
	@Secured("ROLE_ADMIN")
	public ResponseEntity<Map<String, Object>> getAll() {
		Map<String, Object> response = new HashMap<>();
		ResponseService<List<User>> modelResponse = new ResponseService<List<User>>();
		HttpStatus httpStatus;
		
		try {
			modelResponse.setMessage("User found.");
			modelResponse.setInternalMessage("");
			modelResponse.setContent(userService.getAllUsers());
			httpStatus = HttpStatus.OK;
		} catch (DataAccessException e) {
			modelResponse.setMessage("Error get all coupons in database.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse.setMessage("Unknown error.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} finally {
			response.put("message", modelResponse.getMessage());
			response.put("internal_message", modelResponse.getInternalMessage());
			response.put("content", modelResponse.getContent());
		}
		
		return new ResponseEntity<Map<String, Object>>(response, httpStatus);
	}
	
	@GetMapping("/{id}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id ) {
		Map<String, Object> response = new HashMap<String, Object>();
		ResponseService<User> modelResponse = new ResponseService<User>();
		HttpStatus httpStatus;
		
		try {
			User user = userService.findUserById(id);
			if (user != null) {
				modelResponse.setMessage("User found.");
				modelResponse.setInternalMessage("");
				modelResponse.setContent(userService.findUserById(id));
				httpStatus = HttpStatus.OK;
			} else {
				modelResponse.setMessage("User not found.");
				modelResponse.setInternalMessage("user is empty.");
				modelResponse.setContent(null);
				httpStatus = HttpStatus.NOT_FOUND;
			}
			
		} catch (DataAccessException e) {
			modelResponse.setMessage("Error search in database.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		} catch (Exception e) {
			modelResponse.setMessage("Unknown error.");
			modelResponse.setInternalMessage(e.getMessage());
			modelResponse.setContent(null);
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
		} finally {
			response.put("message", modelResponse.getMessage());
			response.put("internal_message", modelResponse.getInternalMessage());
			response.put("content", modelResponse.getContent());
		}
		return new ResponseEntity<Map<String, Object>>(response, httpStatus);
		
	}
}
