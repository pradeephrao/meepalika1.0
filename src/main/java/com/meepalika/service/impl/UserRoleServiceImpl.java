package com.meepalika.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.meepalika.constants.NumericConstants;
import com.meepalika.controller.helper.UserRoleHelper;
import com.meepalika.dao.UserRoleDAO;
import com.meepalika.dto.UserRoleDto;
import com.meepalika.entity.UserRole;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.ResourceNotFoundException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.UserRoleService;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl implements UserRoleService {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);

	@Autowired
	private UserRoleDAO userRoleDAO;

	@Autowired
	private UserRoleHelper userRoleHelper;

	@Override
	@Transactional
	public ApiResponse assignRoles(List<UserRoleDto> userRoleDtoList) {
		logger.info("UserRole service to create roles");
		ApiResponse apiResponse = null;
		try {
			List<UserRole> userRole = new ArrayList<UserRole>(userRoleDtoList.size());
			for (UserRoleDto userRoleDto : userRoleDtoList) {
				userRole.add(userRoleHelper.convertToUserRoleEntity(userRoleDto));
			}
			userRoleDAO.saveAll(userRole);
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_assign_roles");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("role_assigned_successfully"));
		return apiResponse;
	}

	@Override
	public List<UserRoleDto> getByUser(long userId) {
		logger.info("UserRole service to getByUser");
		List<UserRole> userRoleList = null;
		List<UserRoleDto> userRoleDtoList = null;
		ApiResponse apiResponse = null;
		try {
			userRoleList = userRoleDAO.findByUserId(userId);
			if (!userRoleList.isEmpty())
				userRoleDtoList = new ArrayList<UserRoleDto>(userRoleList.size());
			for (UserRole userRole : userRoleList) {
				UserRole role = new UserRole();
				role.setActive(userRole.getActive());
				role.setRole(userRole.getRole());
				role.setId(userRole.getId());
				role.setIsPrimaryRole(userRole.getIsPrimaryRole());
				userRoleDtoList.add(userRoleHelper.convertToUserRoleDto(role));
			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_getuserrole_by_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		if (userRoleDtoList == null || userRoleDtoList.isEmpty()) {
			String message = Translator.toLocale("no_userrole_found_for_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return userRoleDtoList;
	}

	@Override
	public UserRoleDto getUserPrimaryRole(long userId) {
		logger.info("UserRole service to getUserPrimaryRole");
		UserRole userRole = null;
		UserRoleDto userRoleDto = null;
		ApiResponse apiResponse = null;
		try {
			userRole = userRoleDAO.findByUserIdAndIsPrimaryRole(userId, NumericConstants.ACTIVE_DIGIT);
			if (userRole != null)
				userRoleDto = userRoleHelper.convertToUserRoleDto(userRole);
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_getuserrole_by_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		if (userRoleDto == null) {
			String message = Translator.toLocale("no_userrole_found_for_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return userRoleDto;
	}

}
