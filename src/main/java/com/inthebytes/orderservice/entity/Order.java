package com.inthebytes.orderservice.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "`order`")
public class Order implements Serializable {
	
	private static final long serialVersionUID = 9189105789812769743L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
	    name = "UUID",
	    strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "order_id")
	private String id;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "special_instructions")
	private String specialInstructions;
	
	@Column(name = "window_start")
	private Timestamp windowStart;
	
	@Column(name = "window_end")
	private Timestamp windowEnd;
	
	@ManyToOne @JoinColumn(name = "user_id")
	private User customer;
	
	@ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = "destination_id")
	private Location destination;
	
	@ManyToOne @JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
	@OneToMany(mappedBy = "order")
	private List<OrderFood> foods;
	
	@OneToOne(mappedBy = "order", optional = true)
	private Delivery delivery;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public Timestamp getWindowStart() {
		return windowStart;
	}

	public void setWindowStart(Timestamp windowStart) {
		this.windowStart = windowStart;
	}

	public Timestamp getWindowEnd() {
		return windowEnd;
	}

	public void setWindowEnd(Timestamp windowEnd) {
		this.windowEnd = windowEnd;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<OrderFood> getFoods() {
		return foods;
	}

	public void setFoods(List<OrderFood> foods) {
		this.foods = foods;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	
	public User getDriver() {
		return getDelivery().getDriver().getDriver();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
