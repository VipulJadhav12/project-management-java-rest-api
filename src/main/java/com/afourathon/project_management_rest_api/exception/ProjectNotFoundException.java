package com.afourathon.project_management_rest_api.exception;

public class ProjectNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectNotFoundException(String message) {
		super(message);
	}

	public ProjectNotFoundException(Throwable cause) {
		super(cause);
	}

}
