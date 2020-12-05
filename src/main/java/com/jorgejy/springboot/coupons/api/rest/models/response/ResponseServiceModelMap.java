package com.jorgejy.springboot.coupons.api.rest.models.response;

import java.util.HashMap;
import java.util.Map;


public class ResponseServiceModelMap <T> extends  ResponseService<T> {
	
	public ResponseServiceModelMap () {	
	}
	
	public ResponseServiceModelMap(String message, String internalMessage, T content) {
		super(message, internalMessage, content);
	}

	public Map<String, Object> getModelResponse () {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", getMessage());
		response.put("internal_message", getInternalMessage());
		response.put("content", getContent());
		return response;	
	}
}
