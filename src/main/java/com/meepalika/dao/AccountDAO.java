package com.meepalika.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meepalika.entity.Account;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {

	public List<Account> findAll();

	public Optional<Account> findById(Long id);

	public Account save(Account account);

	// public Account update(Account account);

	public void deleteById(Long id);

	public List<Account> findAllByAccountVerified(int accountVerified);

}
