package com.inthebytes.orderservice.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.InvalidSubmissionException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = {EntityNotExistsException.class, EntityNotFoundException.class})
	protected ResponseEntity<Object> handleNotFound(
			EntityNotFoundException exc, WebRequest request) {
	
		String body = "An ID provided in the request body or endpoint does not exist";
        return handleExceptionInternal(exc,	body, 
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(value = NotAuthorizedException.class)
	protected ResponseEntity<Object> handleNotAuthorized(
			NotAuthorizedException exc, WebRequest request) {
		
		String body = "Your account does not have permissions for this operation";
        return handleExceptionInternal(exc,	body, 
          new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}
	
	@ExceptionHandler(value = {
			InvalidSubmissionException.class, 
			SQLIntegrityConstraintViolationException.class,
			IllegalArgumentException.class})
	protected ResponseEntity<Object> handeInvalidBody(
			RuntimeException exc, WebRequest request) {
		
		String body = "Request body improperly formatted or lacks a required property";
        return handleExceptionInternal(exc,	body, 
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}
