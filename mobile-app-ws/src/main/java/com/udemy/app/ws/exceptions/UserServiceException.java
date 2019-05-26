package com.udemy.app.ws.exceptions;

public class UserServiceException extends RuntimeException {
	
	private static final long serialVersionUID = -252015225149435173L;

	public UserServiceException(String message) {
		super(message);
	}
}
