package com.afourathon.project_management_rest_api.exception;

public class ProjectAlreadyExistException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProjectAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectAlreadyExistException(String message) {
		super(message);
	}

	public ProjectAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
