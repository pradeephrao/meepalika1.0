package com.meepalika.controller.helper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meepalika.dto.UserRoleDto;
import com.meepalika.entity.UserRole;

@Component
public class UserRoleHelper {

	@Autowired
	private ModelMapper modelMapper;

	public UserRole convertToUserRoleEntity(UserRoleDto userRoleDto) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		UserRole userRole = modelMapper.map(userRoleDto, UserRole.class);
		return userRole;
	}

	public UserRoleDto convertToUserRoleDto(UserRole userRole) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		UserRoleDto userRoleDto = modelMapper.map(userRole, UserRoleDto.class);
		return userRoleDto;
	}
}
