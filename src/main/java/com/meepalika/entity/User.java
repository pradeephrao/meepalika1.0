package com.meepalika.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.meepalika.utils.CustomDateSerializer;

@Entity
@Table(name = "user")
public class User extends Auditable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotEmpty(message = "username cannot be empty")
	@Column(name = "username")
	private String username;

	@NotEmpty(message = "password cannot be empty")
	@Column(name = "password")
	private String password;

	@NotEmpty(message = "First name cannot be empty")
	@Column(name = "first_name")
	private String first_name;

	@Column(name = "last_name")
	private String last_name;

	@Column(name = "gender")
	private String gender;

	@Column(name = "address")
	private String address;

	@Column(name = "ssn")
	private String ssn;

	@NotEmpty(message = "Country code cannot be empty")
	@Column(name = "country_code")
	private String country_code;

	@Column(name = "city")
	private String city;

	@NotEmpty(message = "Mobile number cannot be empty")
	@Column(name = "mobile_number")
	private String mobile_number;

	@Column(name = "email")
	private String email;

	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(name = "date_of_birth")
	private Date date_of_birth;

	@Column(name = "occupation")
	private String occupation;

	@Column(name = "active")
	private int active;

	@Column(name = "zipcode")
	private String zipcode;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany
	@JoinTable(
			name="user_role",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
	)

	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Role> roles;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotNull(message = "account Id cannot be empty")
	@Column(name = "account_id")
	private long accountId;

	// @NotNull(message = "user registration number cannot be empty")
	@Column(name = "user_registration_number")
	private String user_registration_number;


	@Column(name = "user_media_storage_path")
	private String user_media_storage_path;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getUser_registration_number() {
		return user_registration_number;
	}

	public void setUser_registration_number(String user_registration_number) {
		this.user_registration_number = user_registration_number;
	}

	public String getUser_media_storage_path() {
		return user_media_storage_path;
	}

	public void setUser_media_storage_path(String user_media_storage_path) {
		this.user_media_storage_path = user_media_storage_path;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", first_name=" + first_name
				+ ", last_name=" + last_name + ", gender=" + gender + ", address=" + address + ", ssn=" + ssn
				+ ", mobile_number=" + mobile_number + ", email=" + email + ", date_of_birth=" + date_of_birth
				+ ", occupation=" + occupation + ", active=" + active + ", zipcode=" + zipcode
				+ ", accountId=" + accountId + ", user_registration_number=" + user_registration_number
				+ ", user_media_storage_path=" + user_media_storage_path
				+ ", created_on=" + getCreationDate() + ", modified_on=" + getLastModifiedDate() + ", created_by=" + getCreatedBy()
				+ ", modified_by=" + getLastModifiedBy() + "]";
	}

}
