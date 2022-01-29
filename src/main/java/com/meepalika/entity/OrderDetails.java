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

@Entity
@Table(name = "order_details")
public class OrderDetails extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="status")
    private String status;

    @OneToOne
    @JoinTable(
            name="user",
            joinColumns = @JoinColumn(name="ordered_by")
    )
    private User orderedBy;

    @Column(name="quantity")
    private long quantity;

    @OneToOne
    @JoinTable(
            name="product",
            joinColumns = @JoinColumn(name="productId")
    )
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}