package com.inthebytes.orderservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "delivery")
public class Delivery implements Serializable {

	private static final long serialVersionUID = 4883837882477014012L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
	    name = "UUID",
	    strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "delivery_id")
	private String id;
	
	@OneToOne @JoinColumn(name = "driver_id", referencedColumnName = "driver_id")
	private Driver driver;
	
	@OneToOne @JoinColumn(name = "order_id", referencedColumnName = "order_id")
	private Order order;


	public String getDeliveryId() {
		return id;
	}


	public void setDeliveryId(String deliveryId) {
		this.id = deliveryId;
	}


	public User getDriver() {
		return driver.getDriver();
	}

	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
