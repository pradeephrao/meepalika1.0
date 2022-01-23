package com.meepalika.exception;

import com.meepalika.response.ApiResponse;

public class JwtTokenInvalidException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4374105735877015396L;
	ApiResponse apiResponse;

	public JwtTokenInvalidException(ApiResponse apiResponse) {
		this.apiResponse = apiResponse;
	}

	public ApiResponse getApiResponse() {
		return apiResponse;
	}

}
