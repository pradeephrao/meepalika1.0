package com.meepalika.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.constants.NumericConstants;
import com.meepalika.dto.UserDto;
import com.meepalika.entity.Account;
import com.meepalika.entity.User;
import com.meepalika.entity.UserRole;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.JwtTokenInvalidException;
import com.meepalika.localization.Translator;
import com.meepalika.model.JwtResponse;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.AccountService;
import com.meepalika.service.SmsService;
import com.meepalika.service.UserRoleService;
import com.meepalika.service.UserService;
import com.meepalika.service.helper.UserServiceHelper;

@Service
public class UserSecurityService {

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SmsService smsService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private UserServiceHelper userServiceHelper;

	public JwtResponse signIn(String username, String password) {
		Authentication authenticatedUser = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		User user = (User) authenticatedUser.getDetails();
		Long userId = user.getId();
		String userName = (String) authenticatedUser.getPrincipal();
		Collection<? extends GrantedAuthority> authorities = authenticatedUser.getAuthorities();
		// user is assumed to have atleast one role ROLE_USER
		Collection<String> userRoles = authorities.stream().map(r -> r.getAuthority()).collect(Collectors.toList());
		String token = jwtTokenProvider.createToken(userId, userName, false, userRoles);
		return new JwtResponse(token, Translator.toLocale("authentication_successfull"), user);
	}

	public JwtResponse signUp(User user) {
		UserDto existingUser = null;
		existingUser = userService.getUserByUsername(user.getUsername(),false);
		if(existingUser == null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			ApiResponse apiResponse = userService.createUser(user);
			existingUser = userService.getUserById(apiResponse.getId());
			String token = jwtTokenProvider.createToken(existingUser.getId(), existingUser.getEmail(), false, null);
			user = userServiceHelper.convertToUserEntity(existingUser);

			return new JwtResponse(token, Translator.toLocale("user_created_successfully"), user);
		}
		else {
			ApiResponse apiResponse = new ApiResponse(CODES.FAILURE, Translator.toLocale("user_already_exists"));
			throw new ApplicationException(Translator.toLocale("user_already_exists"), apiResponse);
		}
	}

	public UserDto getUserById(int id) {
		return userService.getUserById(id);
	}

	public UserDto getLoggedInUser(HttpServletRequest req) {
		String jwtToken = jwtTokenProvider.resolveToken(req);
		if (jwtToken == null) {
			ApiResponse apiResponse = new ApiResponse(CODES.FATAL, Translator.toLocale("user_not_loogedin"));
			throw new JwtTokenInvalidException(apiResponse);
		}
		try {
			return userService.getUserByUsername(jwtTokenProvider.getUserId(jwtToken), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
