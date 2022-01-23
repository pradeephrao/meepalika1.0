package com.meepalika.exception;

import java.io.Serializable;

import com.meepalika.response.ApiResponse;

public class ResourceNotFoundException extends RuntimeException implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4047498521984784152L;
	ApiResponse apiResponse;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String msg, ApiResponse apiResponse) {
		super(msg);
		this.apiResponse = apiResponse;
	}

	public ApiResponse getApiResponse() {
		return apiResponse;
	}

}
