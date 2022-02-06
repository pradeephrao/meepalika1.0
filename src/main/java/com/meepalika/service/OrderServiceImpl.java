package com.meepalika.service;

import com.meepalika.dao.OrderDetailsRepository;
import com.meepalika.dao.ShippingAddressRepository;
import com.meepalika.dao.UserDAO;
import com.meepalika.entity.Order;
import com.meepalika.dao.OrderRepository;
import com.meepalika.entity.OrderDetails;
import com.meepalika.entity.ShippingAddress;
import com.meepalika.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Order returnOrder = this.orderRepository.save(order);
        List<OrderDetails> odetails = new ArrayList<>();

        order.getOrderDetails().stream().forEach(od->{
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
}
