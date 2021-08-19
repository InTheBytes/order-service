package com.inthebytes.orderservice.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	private String driverId;
	
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp windowStart;
	
	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((destinationId == null) ? 0 : destinationId.hashCode());
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
		OrderSubmissionDto other = (OrderSubmissionDto) obj;
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
		if (destinationId == null) {
			if (other.destinationId != null)
				return false;
		} else if (!destinationId.equals(other.destinationId))
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
		return "OrderSubmissionDto [destinationId=" + destinationId + ", destination=" + destination + ", customerId="
				+ customerId + ", restaurantId=" + restaurantId + ", items=" + items + ", status=" + status
				+ ", windowStart=" + windowStart + ", windowEnd=" + windowEnd + ", specialInstructions="
				+ specialInstructions + "]";
	}
	
}
