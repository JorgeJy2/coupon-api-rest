package com.jorgejy.springboot.coupons.api.rest.models.response;

public class ResponseService<T> {

	private String message;
	private String internalMessage;
	private T content;

	public ResponseService(){
		
	}
	
	public ResponseService(String message, String internalMessage, T content){
		this.message = message;
		this.internalMessage = internalMessage;
		this.content = content;
	}
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInternalMessage() {
		return internalMessage;
	}

	public void setInternalMessage(String internalMessage) {
		this.internalMessage = internalMessage;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content; 
	}
	
}
