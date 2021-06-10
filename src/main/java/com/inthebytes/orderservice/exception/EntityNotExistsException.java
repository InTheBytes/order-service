package com.inthebytes.orderservice.exception;

public class EntityNotExistsException extends RuntimeException {

	private static final long serialVersionUID = 8594434715685045731L;

	public EntityNotExistsException() {
		super();
	}

	public EntityNotExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EntityNotExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotExistsException(String message) {
		super(message);
	}

	public EntityNotExistsException(Throwable cause) {
		super(cause);
	}
	
	
	
}
