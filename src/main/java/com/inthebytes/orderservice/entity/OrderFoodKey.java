package com.inthebytes.orderservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderFoodKey implements Serializable {

	private static final long serialVersionUID = -3134339846569940109L;

	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "food_id")
	private String foodId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFoodId() {
		return foodId;
	}

	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
