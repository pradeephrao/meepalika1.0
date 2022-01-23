package com.meepalika.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.constants.NumericConstants;
import com.meepalika.dao.UserDAO;
import com.meepalika.dao.UserRoleDAO;
import com.meepalika.dto.UserDto;
import com.meepalika.entity.User;
import com.meepalika.entity.UserRole;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.ResourceNotFoundException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.AccountService;
import com.meepalika.service.FileUploadService;
import com.meepalika.service.UserService;
import com.meepalika.service.helper.UserServiceHelper;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserDAO userDAO;


	@Autowired
	UserServiceHelper userServiceHelper;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	AccountService accountService;

	@Value("${root.storage.path}")
	private String rootStoragePath;

	@Autowired
	private UserRoleDAO userRoleDAO;

	@Override
	@Transactional
	public ApiResponse createUser(User user) {
		logger.info("Executing createUser() in user service");
		ApiResponse apiResponse = null;
		try {
			List<UserRole> userRoleList = user.getUserRole();
			User userToCreate = user;

			checkIfPhoneNumberExists(user);
			user = userDAO.save(user);

			if (user != null && userRoleList != null && !userRoleList.isEmpty()) {
				List<UserRole> userRoles = new ArrayList<UserRole>(userRoleList.size());
				User innerClassObj = user;
				userRoles = userRoleList.stream().map(ur -> {
					UserRole userRole = new UserRole();
					userRole.setUser(innerClassObj);
					userRole.setRole(ur.getRole());
					userRole.setIsPrimaryRole(NumericConstants.ACTIVE_DIGIT);
					userRole.setActive(NumericConstants.ACTIVE_DIGIT);
					return userRole;
				}).collect(Collectors.toList());
				userRoleDAO.saveAll(userRoles);
			}
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_create_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}

		if (user == null) {
			String message = Translator.toLocale("exception_raised_create_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("user_created_successfully"),
				user.getId());
		return apiResponse;
	}

	// update user info
	@Override
	public ApiResponse updateUser(User user, MultipartFile[] documents) {
		logger.info("Executing updateUser() in user service");
		ApiResponse apiResponse = null;
		try {
			User originalUserObj = user;
//			checkIfPhoneNumberExists(user);
			user = userDAO.save(user);
			List<UserRole> userRoleList = getUserRoles(user.getId());
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_update_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}
		if (user == null) {
			String message = Translator.toLocale("exception_raised_update_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("user_updated_successfully"),
				user.getId());
		return apiResponse;
	}

	// this method is called if request has file data, else above method will be
	// called.
	@Override
	public ApiResponse updateUser(User user, MultipartFile file, MultipartFile[] documents) {
		logger.info("Executing updateUser() for file replacement in user service");
		String uploadedPath = fileUploadService.updateUserWithProfile(user.getId(), file);
		user.setUser_media_storage_path(uploadedPath);
		return updateUser(user, documents);
	}

	@Override
	public ApiResponse deleteUser(long id) {
		logger.info("Executing deleteUser() for file deletion in user service");
		ApiResponse apiResponse = null;
		try {
			userDAO.deleteById(id);
		} catch (DataAccessException ex) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_delete_account"));
			throw new ApplicationException(Translator.toLocale("exception_raised_delete_account"), ex, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("account_deleted_successfully"));
		return apiResponse;
	}

	@Override
	public UserDto getUserById(long id) {
		logger.info("Executing getUserById() in user service");
		User user = null;
		Optional<User> userOpt = null;
		UserDto userDto = null;
		ApiResponse apiResponse = null;
		try {
			userOpt = userDAO.findById(id);
			user = userOpt.get();
			if (user != null) {
				user.setUserRole(getUserRoles(user.getId()));
				userDto = userServiceHelper.convertToUserDto(user);
			}
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_fetch_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}

		if (user == null) {
			String message = Translator.toLocale("user_fetch_failed_id") + " " + id;
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return userDto;
	}

	@Override
	public List<UserDto> getAllUsers() {
		logger.info("Executing getAllUsers() in user service");
		List<User> userList = null;
		List<UserDto> userDtoList = null;
		ApiResponse apiResponse = null;
		try {
			userList = userDAO.findAll();
			if(!userList.isEmpty() && userList!=null) {
				userDtoList = new ArrayList<>(userList.size());
				for (User user : userList) {
					userDtoList.add(userServiceHelper.convertToUserDto(user));
				}
			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetch_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		if (userList == null || userList.isEmpty()) {
			String message = Translator.toLocale("user_fetchAll_failed");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return userDtoList;
	}

	@Override
	public UserDto getUserByUsername(String username, boolean accountCreation) {
		logger.info("Executing getUserByUsername() in user service");
		ApiResponse apiResponse = null;
		User user = null;
		UserDto userDto = null;
		Boolean userAccountApproved = false;
		try {
			user = userDAO.findByUsername(username);
			if (user != null) {
				userAccountApproved = accountService.getAccountById(user.getAccountId()).getAccountVerified() > 0 ? true
						: false;
				user.setUserRole(getUserRoles(user.getId()));
				userDto = userServiceHelper.convertToUserDto(user);
			}

		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetch_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}

		if (user != null && !userAccountApproved && !accountCreation) {
			String message = Translator.toLocale("account_approval_pending");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, apiResponse);
		}
		return userDto;
	}


	@Override
	public ApiResponse checkUserNameExists(String username) {
		logger.info("user service to check user name already exists", username);
		ApiResponse apiResponse = null;
		try {
			int userNameCount = userDAO.countByUsername(username);
			if (userNameCount != 0) {
				String message = Translator.toLocale("User_name_already_exists");
				apiResponse = generateApiResponse(CODES.FAILURE, message);
			} else {
				String message = Translator.toLocale("username_not_present");
				apiResponse = generateApiResponse(CODES.SUCCESS, message);
			}
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_checkUserNameExists");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}
		return apiResponse;
	}

	@Transactional
	public ApiResponse updatePassword(String username, String password) {
		logger.info("user service to update password", username);
		ApiResponse apiResponse = null;
		User user = null;
		try {
			user = userDAO.findByUsername(username);
			if (user != null) {
				user.setPassword(userServiceHelper.encryptPassword(password));
				userDAO.save(userDAO.save(user));
				String message = Translator.toLocale("user_password_updated_successfully");
				apiResponse = generateApiResponse(CODES.SUCCESS, message);
			} else {
				String message = Translator.toLocale("updatePassword_username_not_present");
				apiResponse = generateApiResponse(CODES.FAILURE, message);
				throw new ResourceNotFoundException(message, apiResponse);
			}
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_checkUserNameExists");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}
		return apiResponse;
	}

	@Override
	public Map<String, Object> getAllUsersByAccountId(long account_id, int page, int size) {
		logger.info("Executing getAllUsersByAccountId() in user service");
		List<UserDto> allusers = null;
		Map<String, Object> response = new HashMap<>();
		ApiResponse apiResponse = null;
		try {
			Pageable paging = PageRequest.of(page - 1, size, Sort.by("id").descending());
			Page<User> pageAllusers = userDAO.fetchAllusersByAccountIdANDActive(account_id,
					NumericConstants.ACTIVE_DIGIT, paging);
			if(pageAllusers.getContent() != null && pageAllusers.getContent().size()>0) {
				allusers = new ArrayList<UserDto>(pageAllusers.getContent().size());
				for (User user : pageAllusers.getContent()) {
					allusers.add(userServiceHelper.convertToUserDto(user));
				}
			}
			response.put("allusers", allusers);
			response.put("currentPage", pageAllusers.getNumber());
			response.put("totalItems", pageAllusers.getTotalElements());
			response.put("totalPages", pageAllusers.getTotalPages());

		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_fetch_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}

		if (allusers == null) {
			String message = Translator.toLocale("user_fetch_failed_account_id") + " " + account_id;
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return response;
	}


	@Override
	public Set<String> fetchAllSuperAdminEmails(int superAdminRoleId) {
		logger.info("Executing fetchAllSuperAdminEmails()");
		ApiResponse apiResponse = null;
		Set<String> superAdminEmails = null;
		try {
			superAdminEmails = userDAO.fetchAllSuperAdminEmails(superAdminRoleId);
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetch_superAdmins");
			apiResponse = generateApiResponse(CODES.FAILURE, message + ex.getMessage());
			throw new ApplicationException(message, ex, apiResponse);
		}
		if (superAdminEmails == null || superAdminEmails.isEmpty()) {
			String message = Translator.toLocale("user_branch_fetchAll_failed_superAdmin");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}

		return superAdminEmails;
	}
	
	@Override
	public Set<String> fetchAllAccountAdminEmails(int accountId) {
		logger.info("Executing fetchAllAccountAdminEmails()");
		ApiResponse apiResponse = null;
		Set<String> superAdminEmails = null;
		try {
			superAdminEmails = userDAO.fetchAccountAdminEmails(accountId);
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetch_AccountAdmins");
			apiResponse = generateApiResponse(CODES.FAILURE, message + ex.getMessage());
			throw new ApplicationException(message, ex, apiResponse);
		}
		if (superAdminEmails == null || superAdminEmails.isEmpty()) {
			String message = Translator.toLocale("user_branch_fetchAll_failed_AccountAdmin");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}

		return superAdminEmails;
	}

	@Override
	public Map<String, Object> getAllUsersByAccountAndRole(long accountId, long roleId, int active, int page, int size) {
		logger.info("Executing getAllAdminForAccount() in user service");
		List<User> allusers = null;
		Map<String, Object> response = new HashMap<>();
		ApiResponse apiResponse = null;
		try {
			Pageable paging = PageRequest.of(page - 1, size);
			Page<User> pageAllusers = userDAO.findAllByAccountIdAndRoleIdAndActive(accountId, roleId, active, paging);
			allusers = pageAllusers.getContent();
			response.put("allusers", allusers);
			response.put("currentPage", pageAllusers.getNumber());
			response.put("totalItems", pageAllusers.getTotalElements());
			response.put("totalPages", pageAllusers.getTotalPages());

		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_fetch_user");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}

		if (allusers == null) {
			String message = Translator.toLocale("user_fetch_failed_account_id") + " " + accountId;
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return response;
	}

	public List<UserRole> getUserRoles(long userId) {
		List<UserRole> userRoleList = userRoleDAO.findByUserId(userId);
		List<UserRole> userRoles = null;
		if (!userRoleList.isEmpty()) {
			userRoles = new ArrayList<UserRole>(userRoleList.size());
			for (UserRole userRole : userRoleList) {
				UserRole role = new UserRole();
				role.setActive(userRole.getActive());
				role.setRole(userRole.getRole());
				role.setId(userRole.getId());
				role.setIsPrimaryRole(userRole.getIsPrimaryRole());
				userRoles.add(role);
			}
		}
		return userRoles;
	}

	public boolean checkIfPhoneNumberExists(User user) {
		logger.info("Cheking IfPhoneNumberExists() in user service");
		ApiResponse apiResponse = null;
		boolean phNoExists = false;
		User userObj = null;
		try {
			userObj = userDAO.findByMobileNumberAndAccountId(user.getMobile_number(), user.getAccountId());
			if (userObj != null) {
				String message = Translator.toLocale("user_duplicate_phone_number");
				apiResponse = generateApiResponse(CODES.FAILURE, message);
				throw new ApplicationException(message, apiResponse);
			}
		} catch (DataAccessException e) {
			String message = Translator.toLocale("exception_raised_check_duplicate_phno");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, e, apiResponse);
		}
		return phNoExists;
	}


	
	@Override
	public Set<String> fetchAllUserEmailsByIds(List<Long> ids) {
		logger.info("Executing fetchAllUserEmailsByIds()");
		ApiResponse apiResponse = null;
		Set<String> userEmails = null;
		try {
			userEmails = userDAO.findByIdIn(ids);
			System.out.println(userEmails + "User emails");
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_fetch_users");
			apiResponse = generateApiResponse(CODES.FAILURE, message + ex.getMessage());
			throw new ApplicationException(message, ex, apiResponse);
		}
		return userEmails;
	}
	

	
}
