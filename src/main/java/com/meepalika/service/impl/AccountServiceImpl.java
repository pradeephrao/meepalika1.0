package com.meepalika.service.impl;

import java.util.*;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.constants.NumericConstants;
import com.meepalika.dao.AccountDAO;
import com.meepalika.dto.EmailContentDto;
import com.meepalika.dto.UserDto;
import com.meepalika.dto.AdminAccountLinkDto;
import com.meepalika.entity.Account;
import com.meepalika.entity.User;
import com.meepalika.exception.ApplicationException;
import com.meepalika.exception.ResourceNotFoundException;
import com.meepalika.localization.Translator;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.AccountService;
import com.meepalika.service.FileUploadService;
import com.meepalika.service.UserService;
import com.meepalika.service.helper.AccountServiceHelper;
import com.meepalika.service.helper.UserServiceHelper;
import com.meepalika.threads.EmailService;
import com.meepalika.utils.Utils;

@Service
public class AccountServiceImpl extends BaseServiceImpl implements AccountService {
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	AccountDAO accountDAO;

	@Autowired
	UserService userService;

	@Autowired
	EmailService emailService;

	@Autowired
	AccountServiceHelper accountServiceHelper;

	@Autowired
	UserServiceHelper userServiceHelper;

	@Autowired
	FileUploadService fileUploadService;

	@Value("${root.storage.path}")
	private String rootStoragePath;

	@Override
	public ApiResponse createAccount(Account account) {
		logger.info("Account service to create account", account);
		ApiResponse apiResponse = null;
		try {
			accountDAO.save(account);
		} catch (DataAccessException e) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_create_account"));
			throw new ApplicationException(Translator.toLocale("exception_raised_create_account"), e, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("account_created_successfully"));
		return apiResponse;
	}

	@Override
	@Transactional
	public ApiResponse createAccount(AdminAccountLinkDto adminAccountLinkDto, MultipartFile file,
									 MultipartFile[] documents) {
		logger.info("Account service to create account", adminAccountLinkDto);
		ApiResponse apiResponse = null;
		Set<String> fileNames = null;
		try {
			Account account = accountServiceHelper.convertToAccountEntity(adminAccountLinkDto);
			User user = userServiceHelper.convertToUserEntity(adminAccountLinkDto.getUser());
			String uploadedPath = null;
			if (file != null && !file.isEmpty()) {
				uploadedPath = fileUploadService.updateAccountLogo(account.getId(), file);
				account.setLogoStoragePath(uploadedPath);
			}
			account = accountDAO.save(account);
			user.setAccountId(account.getId());
		} catch (DataAccessException e) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_create_account"));
			throw new ApplicationException(Translator.toLocale("exception_raised_create_account"), e, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("account_created_successfully"));
		return apiResponse;
	}

	@Override
	public List<Account> getAllAccounts() {
		logger.info("Account service to getAllAccounts");
		List<Account> accountList = null;
		ApiResponse apiResponse = null;
		try {
			accountList = accountDAO.findAll();
		} catch (DataAccessException ex) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_update_account"));
			throw new ApplicationException(Translator.toLocale("exception_raised_update_account"), ex, apiResponse);
		}

		if (accountList == null || accountList.isEmpty()) {
			String message = Translator.toLocale("account_fetchAll_failed");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return accountList;
	}

	@Override
	public Account getAccountById(Long id) {
		logger.info("Account service to get account by id");
		Account account = null;
		Optional<Account> AcctOPt = null;
		ApiResponse apiResponse = null;
		try {
			AcctOPt = accountDAO.findById(id);
			account = AcctOPt.get();
		} catch (DataAccessException ex) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_fetch_account"));
			throw new ApplicationException(Translator.toLocale("exception_raised_fetch_account"), ex, apiResponse);
		}

		if (account == null) {
			String message = Translator.toLocale("account_fetch_failed_id") + " " + id;
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return account;
	}

	@Override
	public ApiResponse updateAccount(Account account) {
		logger.info("Account service to update account", account);
		ApiResponse apiResponse = null;
		try {
			if (getAccountById(account.getId()) != null) {
				account = accountDAO.saveAndFlush(account);
			}
		} catch (DataAccessException ex) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_update_account"));
			throw new ApplicationException(Translator.toLocale("exception_raised_update_account"), ex, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("account_updated_successfully"));
		return apiResponse;
	}

	@Transactional
	public ApiResponse deleteAccount(long id, long userId) {
		logger.info("Account service to delete Account by Id", id);
		ApiResponse apiResponse = null;
		Set<String> fileNames = new HashSet<>();
		try {
			if (id != 0) {
				Account account = getAccountById(id);
				if (account != null && !Utils.isEmpty(account.getLogoStoragePath())) {
					fileNames.add(account.getLogoStoragePath());
				}
				accountDAO.deleteById(id);
			}
			if (userId != 0) {
				userService.deleteUser(userId);
			}

			if (fileNames.size() > 0) {
				deleteFiles(fileNames);
			}
		} catch (DataAccessException ex) {
			apiResponse = generateApiResponse(CODES.FAILURE, Translator.toLocale("exception_raised_delete_account"));
			throw new ApplicationException(Translator.toLocale("exception_raised_delete_account"), ex, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("account_deleted_successfully"));
		return apiResponse;
	}

	@Override
	@Transactional
	public ApiResponse approveAccount(long accountId, long userId) {
		logger.info("Account service to approve Account and corressponding admin user");
		ApiResponse apiResponse = null;
		try {
			Account account = getAccountById(accountId);
			UserDto user = userService.getUserById(userId);
			account.setAccountVerified(NumericConstants.ACTIVE_DIGIT);
			account = accountDAO.save(account);
			if (account != null) {
				user.setActive(NumericConstants.ACTIVE_DIGIT);
				userService.updateUser(userServiceHelper.convertToUserEntity(user), null);

				String subject = Translator.setPlaceHoldersWithLocale("admin_account_create_approved_subject",
						account.getName());
				String emailBody = Translator.setPlaceHoldersWithLocale("admin_account_create_approved_body",
						user.getUsername());
				EmailContentDto emailContentDto = new EmailContentDto();
				emailContentDto.setTo(user.getEmail());
				emailContentDto.setSubject(subject);
				emailContentDto.setBody(emailBody);
				emailService.sendEmail(emailContentDto);
			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_approve_account");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("account_approved_successfully"));
		return apiResponse;
	}

	@Override
	public List<Account> getAllPendingApprovalAccounts() {
		logger.info("Account service to getAllPendingApprovalAccounts()");
		List<Account> accountList = null;
		ApiResponse apiResponse = null;
		try {
			accountList = accountDAO.findAllByAccountVerified(NumericConstants.INACTIVE_DIGIT);
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_pending_account");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}

		if (accountList == null || accountList.isEmpty()) {
			String message = Translator.toLocale("account_fetchAll_failed");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ResourceNotFoundException(message, apiResponse);
		}
		return accountList;
	}

	@Override
	public AdminAccountLinkDto getAccountAndAdminDetails(long accountId) {
		logger.info("Account service to getAccountAndAdminDetails()");
		Account account = getAccountById(accountId);
		Map<String, Object> userMap = userService.getAllUsersByAccountAndRole(accountId, NumericConstants.ADMIN_ID,
				NumericConstants.INACTIVE_DIGIT, NumericConstants.DEFAULT_PAGE, NumericConstants.DEFAULT_PAGE_SIZE);
		List<User> users = Utils.convertObjectToList(userMap.get("allusers"));
		AdminAccountLinkDto adminAccountLinkDto = new AdminAccountLinkDto(
				accountServiceHelper.convertToAccountDto(account), userServiceHelper.convertToUserDto(users.get(0)));
		return adminAccountLinkDto;
	}

	public ApiResponse uploadAccountDocs(User user, Account account, MultipartFile[] documents) {
		logger.debug("Uplaoding account documents");
		ApiResponse apiResponse = null;

		Set<String> fileNames = null;
		UserDto userDto = null;
		try {
			userDto = userService.getUserByUsername(user.getUsername(), true);
			user = userServiceHelper.convertToUserEntity(userDto);
			if (documents.length > 0) {
				User userObj = user;
			} else {
				String message = Translator.toLocale("files_not_present");
				apiResponse = generateApiResponse(CODES.FAILURE, message);
				throw new ApplicationException(message, apiResponse);
			}
		} catch (DataAccessException ex) {
			String message = Translator.toLocale("exception_raised_approve_account");
			apiResponse = generateApiResponse(CODES.FAILURE, message);
			throw new ApplicationException(message, ex, apiResponse);
		}
		apiResponse = generateApiResponse(CODES.SUCCESS, Translator.toLocale("account_approved_successfully"));
		return apiResponse;
	}
	


}
