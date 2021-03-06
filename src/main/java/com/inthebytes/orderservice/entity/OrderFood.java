package com.inthebytes.orderservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_food")
public class OrderFood implements Serializable {

	public OrderFood() {
		super();
		id = new OrderFoodKey();
	}

	private static final long serialVersionUID = 4793574294278895405L;

	@EmbeddedId
	private OrderFoodKey id;
	
	@ManyToOne(fetch = FetchType.LAZY) 
	@JsonIgnore
	@MapsId("orderId")
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne 
	@MapsId("foodId")
	@JoinColumn(name = "food_id")
	private Food food;
	
	@Column(name = "quantity")
	private Integer quantity;

	public OrderFoodKey getId() {
		return id;
	}

	public void setId(OrderFoodKey id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
		this.id.setOrderId(this.order.getId());
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
		this.id.setFoodId(this.food.getId());
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((food == null) ? 0 : food.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
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
		OrderFood other = (OrderFood) obj;
		if (food == null) {
			if (other.food != null)
				return false;
		} else if (!food.equals(other.food))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
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
		return "OrderFood [id=" + id + ", order=" + order + ", food=" + food + ", quantity=" + quantity + "]";
	}
}
