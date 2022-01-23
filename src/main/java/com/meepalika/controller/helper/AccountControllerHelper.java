package com.meepalika.controller.helper;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepalika.dto.AdminAccountLinkDto;
import com.meepalika.exception.ApplicationException;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;

@Component
public class AccountControllerHelper {

	public AdminAccountLinkDto getJsonUserObject(String account) {
		ObjectMapper objectMapper = new ObjectMapper();
		AdminAccountLinkDto adminAccountLinkDtoJson = null;
		ApiResponse apiResponse = null;
		try {
			adminAccountLinkDtoJson = objectMapper.readValue(account, AdminAccountLinkDto.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			apiResponse = new ApiResponse(CODES.FAILURE, e.getCause().toString());
			throw new ApplicationException(e.getCause().toString(), e, apiResponse);
		} catch (JsonProcessingException e) {
			apiResponse = new ApiResponse(CODES.FAILURE, e.getCause().toString());
			throw new ApplicationException(e.getCause().toString(), e, apiResponse);
		}
		return adminAccountLinkDtoJson;
	}

}
