package com.meepalika.controller;

import com.meepalika.dto.CancelledOrderDetails;
import com.meepalika.entity.Order;
import com.meepalika.entity.Product;
import com.meepalika.service.OrderService;
import com.meepalika.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @PutMapping(value = "/cancel/orderderdetails", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void cancelOrderDetails(@RequestBody CancelledOrderDetails orderDetails){
        orderService.cancelOrderDetails(orderDetails.getIds());
    }



    @PutMapping(value = "/cancel", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void cancelOrder(@RequestBody CancelledOrderDetails orderDetails){
        orderService.cancelOrder(orderDetails.getId());
    }

    @PutMapping(value = "/paysuccess", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void paidOrder(@RequestBody CancelledOrderDetails orderDetails){
        orderService.paidOrder(orderDetails.getId());
    }


    @PutMapping(value = "/shipsuccess", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void shippedOrder(@RequestBody CancelledOrderDetails orderDetails){
        orderService.shippedOrder(orderDetails.getId());
    }
}
