package com.jorgejy.springboot.coupons.api.rest.models;

public class ResponseService<T> {

	private String message;
	private String internalMessage;
	private T content;

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
