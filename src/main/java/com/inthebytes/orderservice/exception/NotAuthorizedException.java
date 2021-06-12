package com.inthebytes.orderservice.exception;

public class NotAuthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1991183870062746825L;

	public NotAuthorizedException() {
		super();
	}

	public NotAuthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAuthorizedException(String message) {
		super(message);
	}

	public NotAuthorizedException(Throwable cause) {
		super(cause);
	}

	
}
