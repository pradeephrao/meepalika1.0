package com.meepalika.dto;

import com.meepalika.entity.Role;
import com.meepalika.entity.User;

public class UserRoleDto {

	private int id;
	private User user;
	private Role role;
	private int isPrimaryRole;
	private int active;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getIsPrimaryRole() {
		return isPrimaryRole;
	}

	public void setPrimaryRole(int isPrimaryRole) {
		this.isPrimaryRole = isPrimaryRole;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
}
