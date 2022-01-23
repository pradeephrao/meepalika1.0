package com.meepalika.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meepalika.entity.Role;
import com.meepalika.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	RoleService roleService;

	@RequestMapping(value = "/getAllRoles", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Role>> getAllRoles() {
		logger.info("role service to be called");
		List<Role> roles = null;
		roles = roleService.getAllRole();
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);

	}

}
