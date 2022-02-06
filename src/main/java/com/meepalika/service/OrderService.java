package com.meepalika.service;

import com.meepalika.entity.Order;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface OrderService {

    @NotNull Order getOrder(Long id);

    Order create(@NotNull(message = "The order cannot be null.") @Valid Order order);

    public void update(@NotNull(message = "The order cannot be null.") @Valid Order order);

    public void cancelOrderDetails(@NotNull(message = "The order cannot be null.") @Valid Long []ids);
}
