package com.meepalika.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.meepalika.dto.UserDto;
import com.meepalika.entity.User;
import com.meepalika.response.ApiResponse;

public interface UserService {

	public ApiResponse createUser(User user);

	public ApiResponse deleteUser(long id);

	public UserDto getUserById(long id);

	public UserDto getUserByUsername(String username, boolean accountCreation);

	public List<UserDto> getAllUsers();


	public ApiResponse updateUser(User user, MultipartFile[] documents);

	public ApiResponse updateUser(User user, MultipartFile file, MultipartFile[] documents);


	public ApiResponse checkUserNameExists(String username);

	public ApiResponse updatePassword(String username, String password);

	public Map<String, Object> getAllUsersByAccountId(long account_id, int page, int size);

	public Set<String> fetchAllSuperAdminEmails(int superAdminRoleId);

	public Map<String, Object> getAllUsersByAccountAndRole(long accountId, long roleId, int active, int page, int size);


	public Set<String> fetchAllAccountAdminEmails(int accountId);

	public Set<String> fetchAllUserEmailsByIds(List<Long> ids);
	

}
