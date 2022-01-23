package com.meepalika.dto;

public class AdminAccountLinkDto {

	private AccountDto account;
	private UserDto user;

	public AdminAccountLinkDto() {
		super();
	}

	public AdminAccountLinkDto(AccountDto account, UserDto user) {
		this.account = account;
		this.user = user;
	}

	public AccountDto getAccount() {
		return account;
	}

	public void setAccount(AccountDto account) {
		this.account = account;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AdminAccountLinkDto [account=" + account + ", user=" + user + "]";
	}

}