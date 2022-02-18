package com.meepalika.service;

import com.meepalika.dao.OrderDetailsRepository;
import com.meepalika.dao.ShippingAddressRepository;
import com.meepalika.dao.UserDAO;
import com.meepalika.entity.*;
import com.meepalika.dao.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements com.meepalika.service.OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    ShippingAddressRepository shippingAddressRepository;

    @Autowired
    UserDAO userDAO;

    @Override
    public Order getOrder(Long id) {

        Optional<Order> orderOpt = this.orderRepository.findById(id);

        ShippingAddress shippingAddress = shippingAddressRepository.findByOrderId(id);

        orderOpt.get().setShippingAddress(shippingAddress);
        return orderOpt.get();
    }

    @Override
    @Transactional
    public Order create(Order order) {
        Optional<User> useropt = userDAO.findById(order.getOrderedBy().getId());
        User user = useropt.get();
        order.setOrderedBy(user);
        order.setStatus(ORDERSTATUS.Ordered);
        Order returnOrder = this.orderRepository.save(order);
        List<OrderDetails> odetails = new ArrayList<>();

        order.getOrderDetails().stream().forEach(od->{
            od.setStatus(ORDERDETAILSTATUS.Ordered);
            od.setOrderId(returnOrder.getId());
            odetails.add(od);
        });
        orderDetailsRepository.saveAll(odetails);

        ShippingAddress shippingAddress = order.getShippingAddress();
        shippingAddress.setOrderId(returnOrder.getId());
        shippingAddressRepository.save(shippingAddress);

        returnOrder.setOrderDetails(odetails);
        returnOrder.setShippingAddress(shippingAddress);
        return order;
    }

    @Override
    public void update(Order order) {
        this.orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrderDetails(List<Long> ids){

        //ArrayList<Long> idsList= (ArrayList<Long>) Arrays.asList(ids);
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findByIdIn(ids);
        List<OrderDetails> saveOrderDetailsList = new ArrayList<>();
        orderDetailsList.stream().forEach(od->{
            od.setStatus(ORDERDETAILSTATUS.Canceled);
            saveOrderDetailsList.add(od);
        });
        orderDetailsRepository.saveAll(saveOrderDetailsList);
    }


    @Override
    @Transactional
    public void cancelOrder(Long id){

        Optional<Order> orderOPt = orderRepository.findById(id);

        Order order = orderOPt.get();

        List<OrderDetails> orderDetailsList = order.getOrderDetails();
        List<OrderDetails> saveOrderDetailsList = new ArrayList<>();
        orderDetailsList.stream().forEach(od->{
            od.setStatus(ORDERDETAILSTATUS.Canceled);
            saveOrderDetailsList.add(od);
        });
        order.setStatus(ORDERSTATUS.Canceled);
        orderRepository.save(order);
        orderDetailsRepository.saveAll(saveOrderDetailsList);

    }

    @Override
    @Transactional
    public void paidOrder(@NotNull(message = "The order to cancel cannot be null.") @Valid Long id){
        Optional<Order> orderOPt = orderRepository.findById(id);

        Order order = orderOPt.get();
        order.setStatus(ORDERSTATUS.Paid);
        orderRepository.save(order);

    }

    @Override
    @Transactional
    public void shippedOrder(@NotNull(message = "The order to cancel cannot be null.") @Valid Long id){
        Optional<Order> orderOPt = orderRepository.findById(id);

        Order order = orderOPt.get();
        order.setStatus(ORDERSTATUS.Shipped);
        orderRepository.save(order);

    }
}
