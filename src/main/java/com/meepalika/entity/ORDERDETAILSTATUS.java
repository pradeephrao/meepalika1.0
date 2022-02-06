package com.meepalika.entity;

public enum ORDERDETAILSTATUS {
	Ordered("Ordered"), Canceled("Cancelled");

	private String status;

	private ORDERDETAILSTATUS(String status) {
		this.status = status;
	}

	public String geStatus() {
		return status;
	}
}