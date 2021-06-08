package com.inthebytes.orderservice.service;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.inthebytes.orderservice.dto.FoodDto;
import com.inthebytes.orderservice.dto.OrderDto;
import com.inthebytes.orderservice.dto.UserDto;
import com.inthebytes.orderservice.entity.Food;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.OrderFood;
import com.inthebytes.orderservice.entity.User;

@Service
public class MapperService {
	
	public Page<OrderDto> convert(Page<Order> orders) {
		return orders.map((x) -> convert(x));
	}
	
	public OrderDto convert(Order entity) {
		OrderDto result = new OrderDto();
		result.setId(entity.getOrderId());
		result.setCustomerId(entity.getCustomer().getUserId());
		result.setRestaurantId(entity.getRestaurant().getRestaurantId());
		result.setItems(entity.getOrderFood()
				.stream()
				.map((x) -> convert(x))
				.collect(Collectors.toList()));
		
		return result;
	}
	
	public Order convert(OrderDto dto) {
		return null;
	}

	private FoodDto convert(OrderFood entity) {
		FoodDto result = new FoodDto();
		result.setId(entity.getFood().getFoodId());
		result.setQuantity(entity.getQuantity());
		return result;
	}
}
