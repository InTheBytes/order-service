package com.inthebytes.orderservice.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.inthebytes.orderservice.dto.group.AdminSubmissionCheck;
import com.inthebytes.orderservice.dto.group.UserSubmissionCheck;

public class ItemDto {
	
	@NotNull(message = "An item must have a corresponding Food ID")
	private String food;
	
	@NotNull(message = "An item must have a quantity")
	private Integer quantity;
	
	@Null(message = "Item submission should only include ID and quantity",
			groups = {AdminSubmissionCheck.class, UserSubmissionCheck.class})
	private String name;
	
	@Null(message = "Item submission should only include ID and quantity",
			groups = {AdminSubmissionCheck.class, UserSubmissionCheck.class})
	private BigDecimal price;

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((food == null) ? 0 : food.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
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
		ItemDto other = (ItemDto) obj;
		if (food == null) {
			if (other.food != null)
				return false;
		} else if (!food.equals(other.food))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemDto [food=" + food + ", quantity=" + quantity + ", name=" + name + ", price=" + price + "]";
	}
}
