package com.meepalika.dto;

import com.meepalika.entity.Product;
import com.meepalika.entity.User;

public class OrderProductDto {

    private Product product;
    private Integer quantity;
    private User orderedBy;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
