package com.meepalika.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meepalika.dto.UserRoleDto;
import com.meepalika.response.ApiResponse;
import com.meepalika.service.UserRoleService;

@RestController
@RequestMapping("/userrole")
public class UserRoleController {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);

	@Autowired
	private UserRoleService userRoleService;

	@PostMapping
	public ResponseEntity<ApiResponse> assignRoles(@RequestBody @Valid List<UserRoleDto> userRoleDtoList) {
		logger.info("Executing asignRoles() in userrole controller");
		return new ResponseEntity<ApiResponse>(userRoleService.assignRoles(userRoleDtoList), HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<UserRoleDto>> getByUser(@PathVariable int userId) {
		logger.info("Executing getByUser() in userrole controller");
		List<UserRoleDto> userRoleDtoList = null;
		userRoleDtoList = userRoleService.getByUser(userId);
		return new ResponseEntity<List<UserRoleDto>>(userRoleDtoList, HttpStatus.OK);
	}

	@GetMapping("/primary/{userId}")
	public ResponseEntity<UserRoleDto> getUserPrimaryRole(@PathVariable int userId) {
		logger.info("Executing getUserPrimaryRole() in userrole controller");
		UserRoleDto userRoleDto = null;
		userRoleDto = userRoleService.getUserPrimaryRole(userId);
		return new ResponseEntity<UserRoleDto>(userRoleDto, HttpStatus.OK);
	}

}
