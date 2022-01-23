package com.meepalika.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meepalika.entity.Country;

@Repository
public interface CountryDAO extends JpaRepository<Country, Long> {

	public Page<Country> findAll(Pageable paging);

}
