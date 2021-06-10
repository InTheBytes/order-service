package com.inthebytes.orderservice.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderDisplayDto {
	
	private String id;
	
	private String status;
	
	private String destination;
	
	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp windowStartTime;
	
	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp windowEndTime;
	
	private String specialInstructions;
	
	private UserDto customer;
	
	private UserDto driver;
	
	private RestaurantDto restaurant;
	
	private List<ItemDto> items;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDestination() {
		return destination;
	}

	public UserDto getCustomer() {
		return customer;
	}

	public void setCustomer(UserDto customer) {
		this.customer = customer;
	}

	public UserDto getDriver() {
		return driver;
	}

	public void setDriver(UserDto driver) {
		this.driver = driver;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public RestaurantDto getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(RestaurantDto restaurant) {
		this.restaurant = restaurant;
	}

	public List<ItemDto> getItems() {
		return items;
	}

	public void setItems(List<ItemDto> items) {
		this.items = items;
	}

	public Timestamp getWindowStartTime() {
		return windowStartTime;
	}

	public void setWindowStartTime(Timestamp windowStartTime) {
		this.windowStartTime = windowStartTime;
	}

	public Timestamp getWindowEndTime() {
		return windowEndTime;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public void setWindowEndTime(Timestamp windowEndTime) {
		this.windowEndTime = windowEndTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((restaurant == null) ? 0 : restaurant.hashCode());
		result = prime * result + ((specialInstructions == null) ? 0 : specialInstructions.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((windowEndTime == null) ? 0 : windowEndTime.hashCode());
		result = prime * result + ((windowStartTime == null) ? 0 : windowStartTime.hashCode());
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
		OrderDisplayDto other = (OrderDisplayDto) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (driver == null) {
			if (other.driver != null)
				return false;
		} else if (!driver.equals(other.driver))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
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
		if (windowEndTime == null) {
			if (other.windowEndTime != null)
				return false;
		} else if (!windowEndTime.equals(other.windowEndTime))
			return false;
		if (windowStartTime == null) {
			if (other.windowStartTime != null)
				return false;
		} else if (!windowStartTime.equals(other.windowStartTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderDisplayDto [id=" + id + ", status=" + status + ", destination=" + destination
				+ ", windowStartTime=" + windowStartTime + ", windowEndTime=" + windowEndTime + ", specialInstructions="
				+ specialInstructions + ", customer=" + customer + ", driver=" + driver + ", restaurant=" + restaurant
				+ ", items=" + items + "]";
	}
}
