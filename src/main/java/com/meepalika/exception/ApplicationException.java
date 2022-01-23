package com.meepalika.exception;

import java.io.Serializable;

import com.meepalika.response.ApiResponse;

public class ApplicationException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = -6643197425265237816L;
	ApiResponse apiResponse;

	public ApplicationException() {
		super();
	}
	public ApplicationException(String msg) {
		super(msg);
	}

	public ApplicationException(String msg, ApiResponse apiResponse) {
		super(msg);
		this.apiResponse = apiResponse;
	}

	public ApplicationException(String msg, Exception e, ApiResponse apiResponse) {
		super(msg, e);
		this.apiResponse = apiResponse;
	}

}
