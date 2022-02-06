package com.meepalika.dao;

import com.meepalika.entity.OrderDetails;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {

	List<OrderDetails> findByIdIn(List<Long> ids);
}
