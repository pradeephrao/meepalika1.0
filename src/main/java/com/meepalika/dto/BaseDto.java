package com.meepalika.dto;

import java.sql.Timestamp;

import com.meepalika.entity.User;

public class BaseDto {

	private Timestamp created_on;
	private Timestamp modifiedOn;
	private User createdBy;
	private User modifiedBy;

	public Timestamp getCreated_on() {
		return created_on;
	}

	public void setCreatedOn(Timestamp created_on) {
		this.created_on = created_on;
	}

	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
