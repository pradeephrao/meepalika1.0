package com.meepalika.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meepalika.entity.UserFileUploadEntity;

@Repository
public interface UserFileUploadDAO extends JpaRepository<UserFileUploadEntity, Long> {

	public UserFileUploadEntity findById(int id);

	public void deleteById(int id);
}
