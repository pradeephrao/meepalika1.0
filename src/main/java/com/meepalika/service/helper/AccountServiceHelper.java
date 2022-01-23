package com.meepalika.service.helper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meepalika.dto.AccountDto;
import com.meepalika.dto.AdminAccountLinkDto;
import com.meepalika.entity.Account;

@Component
public class AccountServiceHelper {

	@Autowired
	private ModelMapper modelMapper;

	public Account convertToAccountEntity(AdminAccountLinkDto adminAccountLinkDto) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		Account account = modelMapper.map(adminAccountLinkDto.getAccount(), Account.class);
		return account;
	}

	public AccountDto convertToAccountDto(Account account) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		AccountDto accountDto = modelMapper.map(account, AccountDto.class);
		return accountDto;
	}
}
