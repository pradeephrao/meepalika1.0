package com.meepalika.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.meepalika.controller.helper.AccountControllerHelper;
import com.meepalika.dto.AdminAccountLinkDto;
import com.meepalika.entity.Account;
import com.meepalika.response.ApiResponse;
import com.meepalika.response.ApiResponse.CODES;
import com.meepalika.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AccountService accountService;

	@Autowired
	AccountControllerHelper accountControllerHelper;

	/*
	 * @PostMapping("/create") public ResponseEntity<ApiResponse>
	 * createAccount(@RequestBody @Valid Account account) {
	 * logger.info("Account to be created"); ApiResponse apiResponse = new
	 * ApiResponse(CODES.FAILURE); apiResponse =
	 * accountService.createAccount(account); return new
	 * ResponseEntity<ApiResponse>(apiResponse,HttpStatus.OK); }
	 */
	@PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ApiResponse> createAccount(@RequestPart @Valid String adminAccount,
													 @RequestParam(value = "file", required = false) MultipartFile file,
													 @RequestParam("documents") MultipartFile[] documents) {
		logger.info("Account to be created");
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE);
		apiResponse = accountService.createAccount(accountControllerHelper.getJsonUserObject(adminAccount), file,
				documents);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse> updateAccount(@RequestBody @Valid Account account) {
		logger.info("Account to be updated");
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE);
		apiResponse = accountService.updateAccount(account);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/allAccounts")
	public ResponseEntity<List<Account>> getAllAccounts() {
		logger.info("Accounts service to be called");
		List<Account> accounts = null;
		accounts = accountService.getAllAccounts();
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);

	}

	@GetMapping("/account/{id}")
	public ResponseEntity<Account> getAllAccounts(@PathVariable long id) {
		logger.info("Account service to be called");
		Account account = null;
		account = accountService.getAccountById(id);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}

	/*
	 * deleting the account will be called only to delete unwanted records used for
	 * develepment,testing Hence same api is used for deleting pending accounts and
	 * its admin user when is no longer required.
	 */
	@DeleteMapping(value = {"/delete/{id}/{userId}", "/delete/{id}"})
	public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long id, @PathVariable Optional<Long> userId) {
		logger.info("Accounts service to be called");
		ApiResponse apiResponse = null;
		if (!userId.isPresent()) {
			apiResponse = accountService.deleteAccount(id, 0);
		} else {
			apiResponse = accountService.deleteAccount(id, userId.get());
		}
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/approveAccount/{id}/{userId}")
	public ResponseEntity<ApiResponse> approveAccount(@PathVariable long id, @PathVariable long userId) {
		logger.info("Account to be approved");
		ApiResponse apiResponse = new ApiResponse(CODES.FAILURE);
		apiResponse = accountService.approveAccount(id, userId);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/allApprovalPendingAccounts")
	public ResponseEntity<List<Account>> getAllApprovalPendingAccounts() {
		logger.info("Accounts service to be called to get all pending accounts for approval");
		List<Account> accounts = null;
		accounts = accountService.getAllPendingApprovalAccounts();
		return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);

	}

	@GetMapping("/approvalPendingAccountAndAdminInfo/{id}")
	public ResponseEntity<AdminAccountLinkDto> getApprovalPendingAccountAndAdminInfo(@PathVariable long id) {
		logger.info("Account service to be called to get approval pending account and admin info");
		AdminAccountLinkDto account = null;
		account = accountService.getAccountAndAdminDetails(id);
		return new ResponseEntity<AdminAccountLinkDto>(account, HttpStatus.OK);
	}
}
