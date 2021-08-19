package com.inthebytes.orderservice.exception;

import javax.persistence.EntityNotFoundException;

public class EntityNotExistsException extends EntityNotFoundException {

	private static final long serialVersionUID = 8594434715685045731L;

	public EntityNotExistsException() {
		super();
	}

	public EntityNotExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
