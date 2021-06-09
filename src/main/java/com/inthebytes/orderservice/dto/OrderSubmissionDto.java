package com.inthebytes.orderservice.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.inthebytes.orderservice.dto.group.AdminSubmissionCheck;
import com.inthebytes.orderservice.dto.group.UserSubmissionCheck;

public class OrderSubmissionDto {
	
	private String destinationId;

	@NotNull(message = "A destination is required for order",
			groups = {AdminSubmissionCheck.class, UserSubmissionCheck.class})
	private LocationDto destination;
	
	@NotNull(message = "A customer ID is required for order",
			groups = {AdminSubmissionCheck.class})
	private String customerId;
	
	@NotNull(message = "A restaurant ID is required for order",
			groups = {AdminSubmissionCheck.class, UserSubmissionCheck.class})
	private String restaurantId;
	
	@NotNull(message = "A list of items of is required for order",
			groups = {AdminSubmissionCheck.class, UserSubmissionCheck.class})
	@NotEmpty(message = "A list of items of is required for order",
			groups = {AdminSubmissionCheck.class, UserSubmissionCheck.class})
	private List<ItemDto> items;
	
	private Integer status;
	
	private Timestamp windowStart;
	
	private Timestamp windowEnd;
	
	private String specialInstructions;

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public LocationDto getDestination() {
		return destination;
	}

	@Valid
	public void setDestination(LocationDto destination) {
		this.destination = destination;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public List<ItemDto> getItems() {
		return items;
	}

	public void setItems(List<ItemDto> items) {
		this.items = items;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	
	
}
