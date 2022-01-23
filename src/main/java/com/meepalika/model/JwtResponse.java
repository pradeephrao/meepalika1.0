package com.meepalika.model;

import java.io.Serializable;

import com.meepalika.entity.User;

public class JwtResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5486141857599308483L;
	private String token;
	private String message;
	private User user;

	public JwtResponse(String token, String message) {
		this.token = token;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getToken() {
		return token;
	}

	public JwtResponse(String token, String message, User user) {
		super();
		this.token = token;
		this.message = message;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
