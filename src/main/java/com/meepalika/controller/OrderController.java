package com.meepalika.controller;

import com.meepalika.entity.Order;
import com.meepalika.entity.Product;
import com.meepalika.service.OrderService;
import com.meepalika.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/orders")

public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/{id}")
    public @NotNull Order getOrders(@PathVariable long  id) {
        return orderService.getOrder(id);
    }


    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addProduct(@RequestBody Order order){
        orderService.create(order);
    }

}
