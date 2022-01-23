package com.meepalika.controller.helper;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepalika.entity.User;
import com.meepalika.exception.ApplicationException;
import com.meepalika.model.UserMedia;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;

@Component
public class UserContollerHelper {

	public User getJsonUserObject(String user) {
		ObjectMapper objectMapper = new ObjectMapper();
		User userJson = null;
		ApiResponse apiResponse = null;
		try {
			userJson = objectMapper.readValue(user, User.class);
		} catch (JsonMappingException e) {
			apiResponse = new ApiResponse(CODES.FAILURE, e.getCause().toString());
			throw new ApplicationException(e.getCause().toString(), e, apiResponse);
		} catch (JsonProcessingException e) {
			apiResponse = new ApiResponse(CODES.FAILURE, e.getCause().toString());
			throw new ApplicationException(e.getCause().toString(), e, apiResponse);
		}
		return userJson;
	}

	public UserMedia getJsonUserMediaObject(String userMedia) {
		ObjectMapper objectMapper = new ObjectMapper();
		UserMedia userMediaJson = null;
		ApiResponse apiResponse = null;
		try {
			userMediaJson = objectMapper.readValue(userMedia, UserMedia.class);
		} catch (JsonMappingException e) {
			apiResponse = new ApiResponse(CODES.FAILURE, e.getCause().toString());
			throw new ApplicationException(e.getCause().toString(), e, apiResponse);
		} catch (JsonProcessingException e) {
			apiResponse = new ApiResponse(CODES.FAILURE, e.getCause().toString());
			throw new ApplicationException(e.getCause().toString(), e, apiResponse);
		}
		return userMediaJson;
	}
}
