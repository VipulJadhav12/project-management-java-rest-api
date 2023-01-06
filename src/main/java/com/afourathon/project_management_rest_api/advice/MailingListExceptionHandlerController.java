package com.afourathon.project_management_rest_api.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.afourathon.project_management_rest_api.exception.EmailNotFoundException;

@RestControllerAdvice
public class MailingListExceptionHandlerController {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleApiMethodInvalidArgumentException(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error ->
			errorMap.put(error.getField(), error.getDefaultMessage())
		);

		return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleException(EmailNotFoundException ex) {
		Map<String, String> errorMap = new HashMap<>();

		errorMap.put("status", HttpStatus.NOT_FOUND.toString());
		errorMap.put("message", ex.getMessage());

		return new ResponseEntity<>(errorMap, HttpStatus.NOT_FOUND);
	}

}
