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
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
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
}
