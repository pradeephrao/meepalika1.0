package com.meepalika.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.meepalika.entity.Role;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.meepalika.dto.UserDto;
import com.meepalika.entity.User;
import com.meepalika.entity.UserRole;
import com.meepalika.service.UserRoleService;
import com.meepalika.service.UserService;
import com.meepalika.service.helper.UserServiceHelper;

@Service
public class UserAccessDetailsService implements UserDetailsService {

	@Autowired
	UserService userService;

	@Autowired
	UserRoleService userRoleService;
	
	@Autowired
	UserServiceHelper userServiceHelper;

	@SuppressWarnings("serial")
	public static class UserRoleAccess implements GrantedAuthority {

		enum ROLES {
			ADMIN, ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_DOCTOR, ROLE_NURSE, ROLE_PATIENT, ROLE_RECEPTIONIST, ROLE_ANONNYMOUS,
			ROLE_LABTECHNICIAN, ROLE_PHARMACIST, ROLE_ACCOUNTANT, ROLE_OTHER, ROLE_CASEWORKER, ROLE_USER
		}

		private ROLES role;

		public UserRoleAccess(String stringRole) {
			this.role = ROLES.valueOf(stringRole);
		}

		public UserRoleAccess(ROLES role) {
			this.role = role;
		}

		@Override
		public String getAuthority() {
			return role.toString();
		}

		public static Collection<UserRoleAccess> getRoles(Collection<String> roleValues) {
			if (roleValues != null) {
				return roleValues.stream().map(role -> new UserRoleAccess(role)).collect(Collectors.toList());
			} else {
				return null;
			}
		}

		public static Collection<UserRoleAccess> getAnonymousRole() {
			return Arrays.asList(new UserRoleAccess(ROLES.ROLE_ANONNYMOUS));
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(UserAccessDetailsService.class);

	static final User ANONYMOUS = new User();
	public static final Long ANONYMOUS_ID = -1L;
	public static final String ANONYMOUS_EMAIL = "anonymous@hms.com";
	public static final String ANONYMOUS_USERNAME = "ANONYMOUS_NAME";

	static {
		ANONYMOUS.setUsername(ANONYMOUS_USERNAME);
		ANONYMOUS.setId(ANONYMOUS_ID);
		ANONYMOUS.setEmail(ANONYMOUS_EMAIL);
		ANONYMOUS.setPassword(ANONYMOUS_EMAIL);
		// anoymous user is always active
		ANONYMOUS.setActive(1);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		UserDto userDto = null;
		if (username.equalsIgnoreCase(String.valueOf(ANONYMOUS_ID))) {
			user = ANONYMOUS;
			username = user.getUsername();
		} else {
			userDto = userService.getUserById(Long.parseLong(username));
			user = userServiceHelper.convertToUserEntity(userDto);
		}
		Collection<UserRoleAccess> userRoles = null;
		List<Role> userRoleList = user.getRoles();
		if (userRoleList != null && !userRoleList.isEmpty()) {
			Collection<String> roles = new ArrayList<String>(userRoleList.size());
			for (Role userRole : userRoleList) {
				roles.add(userRole.getName());
			}
			userRoles = UserRoleAccess.getRoles(roles);
		} else {
			userRoles = Arrays.asList(new UserRoleAccess(UserRoleAccess.ROLES.ROLE_ANONNYMOUS));
		}
		MDC.put("loggedInUser", user.getId());
		return org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword())
				.authorities(userRoles).accountExpired(false).accountLocked(false).credentialsExpired(false)
				.disabled(false).build();
	}

}
