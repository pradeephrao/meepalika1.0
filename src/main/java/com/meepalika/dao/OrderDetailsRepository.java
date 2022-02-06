package com.meepalika.dao;

import com.meepalika.entity.OrderDetails;

import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Long> {
}
