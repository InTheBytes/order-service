package com.inthebytes.orderservice.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "restaurant")
public class Restaurant implements Serializable {

	private static final long serialVersionUID = -8585743503516197472L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String restaurantId;

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
