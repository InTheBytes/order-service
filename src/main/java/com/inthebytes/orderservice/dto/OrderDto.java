package com.inthebytes.orderservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


public class OrderDto {
	
	@Null(message = "ID can not be chosen, it will be generated",
			groups = SubmitChecks.class)
	private String id;
	
	@NotNull
	private String customerId;
	
	@NotNull
	private String restaurantId;
	
	@NotNull
	private LocationDto destination;
	
	@NotNull
	@NotEmpty
	private List<FoodDto> items;
	
	@NotNull
	private LocalDateTime windowStart;
	
	@NotNull
	private LocalDateTime windowEnd;

	@Null(message = "Status will be automatically set to created",
			groups = SubmitChecks.class)
	private Integer status;

	private String specialInstructions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customer) {
		this.customerId = customer;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurant) {
		this.restaurantId = restaurant;
	}

	public LocationDto getDestination() {
		return destination;
	}

	public void setDestination(LocationDto destination) {
		this.destination = destination;
	}

	public List<FoodDto> getItems() {
		return items;
	}

	public void setItems(List<FoodDto> items) {
		this.items = items;
	}

	public LocalDateTime getWindowStart() {
		return windowStart;
	}

	public void setWindowStart(LocalDateTime windowStart) {
		this.windowStart = windowStart;
	}

	public LocalDateTime getWindowEnd() {
		return windowEnd;
	}

	public void setWindowEnd(LocalDateTime windowEnd) {
		this.windowEnd = windowEnd;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((restaurantId == null) ? 0 : restaurantId.hashCode());
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
		OrderDto other = (OrderDto) obj;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
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
		if (restaurantId == null) {
			if (other.restaurantId != null)
				return false;
		} else if (!restaurantId.equals(other.restaurantId))
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
		return "OrderDto [id=" + id + ", customerId=" + customerId + ", restaurantId=" + restaurantId + ", destination="
				+ destination + ", items=" + items + ", windowStart=" + windowStart + ", windowEnd=" + windowEnd
				+ ", status=" + status + ", specialInstructions=" + specialInstructions + "]";
	}
}
