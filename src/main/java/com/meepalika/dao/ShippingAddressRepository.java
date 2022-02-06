package com.meepalika.dao;

import com.meepalika.entity.Order;
import com.meepalika.entity.ShippingAddress;
import org.springframework.data.repository.CrudRepository;

public interface ShippingAddressRepository extends CrudRepository<ShippingAddress, Long> {

	public ShippingAddress findByOrderId(Long id);
}
