package com.meepalika.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.meepalika.entity.User;
import com.meepalika.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.controller.helper.UserContollerHelper;
import com.meepalika.dto.UserDto;
import com.meepalika.model.JwtResponse;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.security.UserSecurityService;
import com.meepalika.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private UserContollerHelper userControllerHelper;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestParam String username, @RequestParam String password) {
		logger.info("Executing login() in user controller");

		return new ResponseEntity<JwtResponse>(userSecurityService.signIn(username, password), HttpStatus.OK);
	}

	@PostMapping("/register")
	public String createUser(@RequestBody User user) {
		logger.info("create user" + user.toString());
		logger.debug("Has Role ADMIN" + request.isUserInRole("ROLE_ADMIN"));
		logger.debug("Has Role USER " + request.isUserInRole("ROLE_USER"));
		if(!(request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER"))) {
			throw new ApplicationException("User does not have the relevant permission to make this request");
		}
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE);
		String jwtToken = "";
		JwtResponse jwtReponse = null;
		try {
			jwtReponse = userSecurityService.signUp(user);
		} catch (ApplicationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		if(!apiResponse.isSucessful()) {

		}
		return jwtReponse.getToken();
	}

	@PutMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE})
	/*
	 * user object from client sent as request part(form data) is throwing 400
	 * error.For now its changed to String we have to look into this later
	 */
	public ResponseEntity<ApiResponse> updateUser(@RequestBody User user) {
		logger.info("Executing update User() in user controller");
			return new ResponseEntity<ApiResponse>(
					userService.updateUser(user, null, null),
					HttpStatus.OK);
	}

	@GetMapping("/account/{account_id}")
	public ResponseEntity<Map<String, Object>> getAllUsersByAccountId(@PathVariable int account_id,
																	  @RequestParam("page") int page, @RequestParam("size") int size) {
		logger.info("User service to be called");
		Map<String, Object> allUsers = null;
		allUsers = userService.getAllUsersByAccountId(account_id, page, size);
		return new ResponseEntity<Map<String, Object>>(allUsers, HttpStatus.OK);
	}

	// get registered user by id
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
		logger.info("Account service to be called");
		UserDto user = userService.getUserById(id);
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}


	@GetMapping(value = "/exists/{username}")
	public ResponseEntity<ApiResponse> checkUserNameExists(@PathVariable String username) {
		logger.info("user service to check user already exists", username);
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE);
		apiResponse = userService.checkUserNameExists(username);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);

	}

	@PutMapping(value = "/updatePassword/{username}/{password}")
	public ResponseEntity<ApiResponse> checkUserNameExists(@PathVariable String username,
														   @PathVariable String password) {
		logger.info("user service to update password", username);
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE);
		apiResponse = userService.updatePassword(username, password);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}



}
