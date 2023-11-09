package com.afourathon.project_management_rest_api.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.afourathon.project_management_rest_api.exception.ProjectNotFoundException;

@RestControllerAdvice
public class ProjectExceptionHandlerController {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleApiMethodInvalidArgumentException(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});

		return errorMap;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ProjectNotFoundException.class)
	public Map<String, String> handleException(ProjectNotFoundException ex) {
		Map<String, String> errorMap = new HashMap<>();

		errorMap.put("errorMessage", ex.getMessage());

		return errorMap;
	}

}
