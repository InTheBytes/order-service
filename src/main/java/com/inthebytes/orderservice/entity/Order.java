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
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((delivery == null) ? 0 : delivery.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((foods == null) ? 0 : foods.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((restaurant == null) ? 0 : restaurant.hashCode());
		result = prime * result + ((specialInstructions == null) ? 0 : specialInstructions.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((windowEnd == null) ? 0 : windowEnd.hashCode());
		result = prime * result + ((windowStart == null) ? 0 : windowStart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (delivery == null) {
			if (other.delivery != null)
				return false;
		} else if (!delivery.equals(other.delivery))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (foods == null) {
			if (other.foods != null)
				return false;
		} else if (!foods.equals(other.foods))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (restaurant == null) {
			if (other.restaurant != null)
				return false;
		} else if (!restaurant.equals(other.restaurant))
			return false;
		if (specialInstructions == null) {
			if (other.specialInstructions != null)
				return false;
		} else if (!specialInstructions.equals(other.specialInstructions))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (windowEnd == null) {
			if (other.windowEnd != null)
				return false;
		} else if (!windowEnd.equals(other.windowEnd))
			return false;
		if (windowStart == null) {
			if (other.windowStart != null)
				return false;
		} else if (!windowStart.equals(other.windowStart))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", status=" + status + ", specialInstructions=" + specialInstructions
				+ ", windowStart=" + windowStart + ", windowEnd=" + windowEnd + ", customer=" + customer
				+ ", destination=" + destination + ", restaurant=" + restaurant + ", foods=" + foods + ", delivery="
				+ delivery + "]";
	}
}
