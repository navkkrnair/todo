package com.cts.error;

public class NoEntityFoundException extends RuntimeException {
	public NoEntityFoundException(String message) {
		super(message);
	}
}
