package com.meepalika.exception;

import java.io.Serializable;

import com.meepalika.response.ApiResponse;

public class AccessDeniedException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = -6643197425265237816L;
	ApiResponse apiResponse;

	public AccessDeniedException() {
		super();
	}

	public AccessDeniedException(String msg, ApiResponse apiResponse) {
		super(msg);
		this.apiResponse = apiResponse;
	}

	public AccessDeniedException(String msg, Exception e, ApiResponse apiResponse) {
		super(msg, e);
		this.apiResponse = apiResponse;
	}

}
