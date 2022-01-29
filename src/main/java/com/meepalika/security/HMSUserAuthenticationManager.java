package com.meepalika.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.meepalika.entity.Role;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.meepalika.constants.NumericConstants;
import com.meepalika.dto.UserDto;
import com.meepalika.exception.AccessDeniedException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.security.UserAccessDetailsService.UserRoleAccess;
import com.meepalika.service.UserRoleService;
import com.meepalika.service.UserService;
import com.meepalika.service.helper.UserServiceHelper;

public class HMSUserAuthenticationManager implements AuthenticationManager {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	UserRoleService userRoleService;
	
	@Autowired
	UserServiceHelper userServiceHelper;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();
		UserDto userDto = userService.getUserByUsername(userName, false);
		if (userDto == null) {
			throw new AuthenticationCredentialsNotFoundException(
					Translator.toLocale("invalid_user_name") + " " + userName);
		}
		if (userDto.getActive() != NumericConstants.ACTIVE_DIGIT) {
			String message = Translator.setPlaceHoldersWithLocale("user_username_deactivated", userDto.getUsername());
			ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, message);
			throw new AccessDeniedException(message, apiResponse);
		}
		String dbPassword = userDto.getPassword();
		boolean matched = this.passwordEncoder.matches(password, dbPassword);
		if (!matched) {
			throw new AuthenticationCredentialsNotFoundException(
					Translator.toLocale("invalid_password") + " " + userName);
		}

		Collection<UserRoleAccess> userRoles = null;
		List<Role> userRoleList = userDto.getRoles();
		Collection<String> roles = new ArrayList<String>(userRoleList.size());
		for (Role userRole : userRoleList) {
			roles.add(userRole.getName());
		}
		if (roles == null || !roles.isEmpty()) {
			if (roles == null || roles.size() == 0)
				userRoles = Arrays.asList(new UserRoleAccess(userRoleService.getUserPrimaryRole(userDto.getId()).getRole().getName()));
			else
				userRoles = UserRoleAccess.getRoles(roles);
		}
		UsernamePasswordAuthenticationToken hmsUser = new UsernamePasswordAuthenticationToken(userName, password,
				userRoles);
		hmsUser.setDetails(userServiceHelper.convertToUserEntity(userDto));
		MDC.put("loggedInUser", userDto.getId());
		return hmsUser;
	}

}
