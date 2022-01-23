package com.meepalika.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meepalika.entity.Role;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {

	public List<Role> findAllByIdIn(int[] roleIds);

}
