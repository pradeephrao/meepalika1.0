package com.meepalika.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Table(name = "order_details")
public class OrderDetails extends Auditable<Long> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ORDERDETAILSTATUS status;

    @Column(name="order_id")
    private Long orderId;


    @OneToOne
    @JoinColumn(name = "ordered_by", referencedColumnName = "id")
    private User orderedBy;

    @Column(name="quantity")
    private long quantity;

    @OneToOne
    @JoinColumn(name = "productId", referencedColumnName = "id")
    private Product product = new Product();

    public void setOrderedBy(User orderedBy) {
        this.orderedBy = orderedBy;
    }

    public long getQuantity() {
        return quantity;
    }


    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }



    public User getOrderedBy() {
        return orderedBy;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ORDERDETAILSTATUS getStatus() {
        return status;
    }

    public void setStatus(ORDERDETAILSTATUS status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }



}
