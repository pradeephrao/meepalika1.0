package com.meepalika.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.meepalika.dto.AdminAccountLinkDto;
import com.meepalika.entity.Account;
import com.meepalika.response.ApiResponse;

public interface AccountService {

	public ApiResponse createAccount(Account account);

	public List<Account> getAllAccounts();

	public Account getAccountById(Long id);

	public ApiResponse updateAccount(Account account);

	public ApiResponse deleteAccount(long id, long userId);

	public ApiResponse createAccount(AdminAccountLinkDto adminAccountLinkDto, MultipartFile file,
									 MultipartFile[] documents);

	public ApiResponse approveAccount(long accountId, long userId);

	public List<Account> getAllPendingApprovalAccounts();

	public AdminAccountLinkDto getAccountAndAdminDetails(long accountId);


}
