package com.meepalika.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.meepalika.dao.RoleDAO;
import com.meepalika.entity.Role;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.ResourceNotFoundException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.RoleService;

@Service
public class RoleServiceImpl extends BaseServiceImpl implements RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Autowired
	RoleDAO roleDAO;

	@Override
	public List<Role> getAllRole() {
		logger.info("Role service to getAllRoles");
		List<Role> roleList = null;
		ApiResponse apiResponse = null;
		try {
			int[] roleIds = {1, 2, 4, 5, 7, 11};
			roleList = roleDAO.findAllByIdIn(roleIds);
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetchAll_Role");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(Translator.toLocale("exception_raised_fetchAll_Role"), ex, apiResponse);
		}
		if (roleList == null || roleList.isEmpty()) {
			String message = Translator.toLocale("Role_fetchAll_failed");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(Translator.toLocale("Role_fetchAll_failed"), apiResponse);
		}
		return roleList;
	}

}
