package com.meepalika.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Table(name = "myorder")
public class Order extends Auditable<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="status")
    private String status;

    @Transient
    public Double getTotalOrderPrice() {
        AtomicReference<Double> sum = new AtomicReference<>(0D);
        orderDetails.stream().forEach((k) -> {
            sum.updateAndGet(v -> (v + k.getQuantity() * k.getProduct().getPrice()));
        });;
        return sum.get();
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "orderedBy", referencedColumnName = "id")
    private User orderedBy;

    @OneToMany(fetch = FetchType.LAZY,  cascade = {CascadeType.MERGE})
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<OrderDetails> orderDetails;


    @Transient
    private ShippingAddress shippingAddress;

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> _orderDetails) {
        this.orderDetails = _orderDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(User orderedBy) {
        this.orderedBy = orderedBy;
    }




    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }
}
