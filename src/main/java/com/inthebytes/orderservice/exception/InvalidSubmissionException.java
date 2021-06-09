package com.inthebytes.orderservice.exception;

public class InvalidSubmissionException extends Exception {

	private static final long serialVersionUID = 109006903040925934L;

	public InvalidSubmissionException() {
		super();
	}

	public InvalidSubmissionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidSubmissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSubmissionException(String message) {
		super(message);
	}

	public InvalidSubmissionException(Throwable cause) {
		super(cause);
	}
	
	

}
