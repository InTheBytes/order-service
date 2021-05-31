package com.inthebytes.orderservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity @Table(name = "delivery")
public class Delivery implements Serializable {

	private static final long serialVersionUID = 4883837882477014012L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private String deliveryId;
	
	@JoinColumn(name = "driver_id")
	private Driver driver;
	
	
	@Entity @Table(name = "driver")
	private class Driver {
		
		@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "restaurantId")
		private String driverId;
		
		@OneToOne @JoinColumn(name = "user_id")
		private User driver;

		public User getDriver() {
			return driver;
		}
	}


	public String getDeliveryId() {
		return deliveryId;
	}


	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}


	public User getDriver() {
		return driver.getDriver();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
