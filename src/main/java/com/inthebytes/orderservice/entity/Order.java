package com.inthebytes.orderservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order")
public class Order implements Serializable {

	private static final long serialVersionUID = 3110119306284727886L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "user_id")
	private User customer;
	
	@Column(name = "restaurant_id")
	private Restaurant restaurant;
	
	@Column(name = "driver_id")
	private Delivery delivery;
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public User getDriver() {
		return delivery.getDriver();
	}

	public void setDriver(Delivery delivery) {
		this.delivery = delivery;
	}
}
