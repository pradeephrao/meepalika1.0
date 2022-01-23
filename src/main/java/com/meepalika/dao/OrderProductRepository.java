package com.meepalika.dao;

import com.meepalika.entity.OrderProduct;
import com.meepalika.entity.OrderProductPK;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {
}
