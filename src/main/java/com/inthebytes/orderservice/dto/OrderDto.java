package com.inthebytes.orderservice.dto;

import java.sql.Timestamp;
import java.util.List;

public class OrderDto {
	
	private String id;
	
	private String status;
	
	private String destination;
	
	private Timestamp windowStartTime;
	
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
}
