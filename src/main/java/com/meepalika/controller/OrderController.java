package com.meepalika.controller;

import com.meepalika.entity.Order;
import com.meepalika.entity.Product;
import com.meepalika.service.OrderService;
import com.meepalika.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping(value = { "", "/" })
    public @NotNull Iterable<Order> getOrders() {
        return orderService.getAllOrders();
    }


    @PostMapping(value = "")
    public void addProduct(@RequestBody Order order){
        orderService.create(order);
    }

}
