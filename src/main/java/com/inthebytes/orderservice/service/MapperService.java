package com.inthebytes.orderservice.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.inthebytes.orderservice.dto.OrderDto;
import com.inthebytes.orderservice.dto.UserDto;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.User;

@Component
public class MapperService {
	
	public OrderDto convert(Order entity) {
		
		return null;
	}
	
	public Order convert(OrderDto dto) {
		return null;
	}
	
	public Page<OrderDto> convert(Page<Order> orders) {
		return orders.map((x) -> convert(x));
	}
	
	public UserDto convert(User entity) {
		
		return null;
	}

}
