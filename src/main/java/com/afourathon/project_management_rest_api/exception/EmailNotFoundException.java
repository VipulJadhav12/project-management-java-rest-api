package com.afourathon.project_management_rest_api.exception;

public class EmailNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public EmailNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailNotFoundException(String message) {
		super(message);
	}

	public EmailNotFoundException(Throwable cause) {
		super(cause);
	}

}
