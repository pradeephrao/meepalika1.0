package com.meepalika.dto;

import javax.validation.constraints.NotEmpty;

public class AccountDto {

	private int id;

	@NotEmpty(message = "Account Name cannot be empty")
	private String name;

	private String logoStoragePath;

	private Integer account_verified;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoStoragePath() {
		return logoStoragePath;
	}

	public void setLogoStoragePath(String logoStoragePath) {
		this.logoStoragePath = logoStoragePath;
	}


	public Integer getAccount_verified() {
		return account_verified;
	}

	public void setAccount_verified(Integer account_verified) {
		this.account_verified = account_verified;
	}

	@Override
	public String toString() {
		return "AccountDto [id=" + id + ", name=" + name + ", logoStoragePath=" + logoStoragePath  + "]";
	}

}
