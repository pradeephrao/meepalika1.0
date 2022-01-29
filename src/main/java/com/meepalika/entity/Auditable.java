package com.meepalika.entity;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.MDC;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


//spring auditing annotations
//annotation designates a class whose mapping information is applied to the
//entities that inherit from it. A mapped super class has no separate table defined
//for it
@MappedSuperclass
//specifies the callback listener classes to be used for an entity or mapped
//superclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> {

	//updatable flag helps to avoid the override of
	//column's value during the update operation
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private U createdBy;

	@CreatedDate
	@Column(name = "created_on", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date creationDate;

	@LastModifiedBy
	@Column(name = "modified_by")
	private U lastModifiedBy;

	@LastModifiedDate
	@Column(name = "modified_on")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;


	public U getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(U createdBy) {
		if(MDC.get("loggedInUser") != null){
			Long userID = Long.parseLong(MDC.get("loggedInUser"));
		}

	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public U getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(U lastModifiedBy) {
		if(MDC.get("loggedInUser") != null){
			Long userID = Long.parseLong(MDC.get("loggedInUser"));
		}

		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	//updatable flag helps to avoid the override of
	//column's value during the update operation

}