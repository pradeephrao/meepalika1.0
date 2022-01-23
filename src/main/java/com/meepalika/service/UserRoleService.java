package com.meepalika.service;

import java.util.List;

import com.meepalika.dto.UserRoleDto;
import com.meepalika.response.ApiResponse;

public interface UserRoleService {

	ApiResponse assignRoles(List<UserRoleDto> userRoleDtoList);

	List<UserRoleDto> getByUser(long userId);

	UserRoleDto getUserPrimaryRole(long userId);


}
