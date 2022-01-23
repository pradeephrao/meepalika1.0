package com.meepalika.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "account")

public class Account extends Auditable<Long>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "Name cannot be empty")
	@Column(name = "name")
	private String name;

	@Column(name = "logo_storage_path")
	private String logoStoragePath;


	@Column(name = "account_verified")
	private int accountVerified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public int getAccountVerified() {
		return accountVerified;
	}

	public void setAccountVerified(int accountVerified) {
		this.accountVerified = accountVerified;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", logoStoragePath=" + logoStoragePath + ", accountVerified="
				+ accountVerified + "]";
	}

}
