package com.meepalika.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meepalika.entity.UserRole;

@Repository
public interface UserRoleDAO extends JpaRepository<UserRole, Long> {

	List<UserRole> findByUserId(long userId);

	UserRole findByUserIdAndIsPrimaryRole(long userId, int activeDigit);
}
