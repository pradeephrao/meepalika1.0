package com.meepalika.entity;


public enum ORDERSTATUS {
    Ordered("Ordered"), Canceled("Cancelled"), Shipped("Shipped"), Paid("Paid");

    private String status;

    private ORDERSTATUS(String status) {
        this.status = status;
    }

    public String geStatus() {
        return status;
    }
}

