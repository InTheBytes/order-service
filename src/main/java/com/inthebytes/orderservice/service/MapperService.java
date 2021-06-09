package com.inthebytes.orderservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.inthebytes.orderservice.dto.ItemDto;
import com.inthebytes.orderservice.dto.OrderDto;
import com.inthebytes.orderservice.dto.RestaurantDto;
import com.inthebytes.orderservice.dto.UserDto;
import com.inthebytes.orderservice.entity.Location;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.OrderFood;
import com.inthebytes.orderservice.entity.Restaurant;
import com.inthebytes.orderservice.entity.User;

@Service
public class MapperService {
	
	public Page<OrderDto> convert(Page<Order> orders) {
		return orders.map((x) -> convert(x));
	}
	
	public OrderDto convert(Order entity) {
		if (entity == null)
			return null;
		OrderDto order = new OrderDto();
		order.setId(entity.getId());
		order.setDestination(convert(entity.getDestination()));
		order.setItems(convert(entity.getFoods()));
		order.setCustomer(convert(entity.getCustomer()));
		order.setDriver(convert(entity.getDelivery().getDriver().getDriver()));
		order.setRestaurant(convert(entity.getRestaurant()));
		order.setWindowStartTime(entity.getWindowStart());
		order.setWindowEndTime(entity.getWindowEnd());
		order.setSpecialInstructions(entity.getSpecialInstructions());
		
		String status;
		switch (entity.getStatus()) {
		case 0:
			status = "- CREATED";
			break;
		case 1:
			status = "- PAID";
			break;
		case 2:
			status = "- STARTED";
			break;
		case 3:
			status = "- IN TRANSIT";
			break;
		case 4:
			status = "- COMPLETE";
			break;
		case 5: 
			status = "- CANCELLED";
			break;
		default:
			status = "";
		}
		order.setStatus(String.format("%d %s", entity.getStatus(), status));
		return order;
	}
	
	private List<ItemDto> convert(List<OrderFood> foods) {
		return foods.stream()
				.map((x) -> convert(x))
				.collect(Collectors.toList());
	}
	
	private ItemDto convert(OrderFood food) {
		ItemDto item = new ItemDto();
		item.setFood(food.getFood().getId());
		item.setQuantity(food.getQuantity());
		return item;
	}
	
	private UserDto convert(User entity) {
		UserDto user = new UserDto();
		user.setId(entity.getId());
		user.setName(String.format("%s %s", entity.getFirstName(), entity.getLastName()));
		return user;
	}
	
	private RestaurantDto convert(Restaurant entity) {
		RestaurantDto restaurant = new RestaurantDto();
		restaurant.setId(entity.getId());
		restaurant.setLocation(convert(entity.getLocation()));
		restaurant.setName(entity.getName());
		return restaurant;
	}
	
	private String convert(Location entity) {
		return String.format("%s %s, %s, %s %d", 
				entity.getUnit(), 
				entity.getStreet(), 
				entity.getCity(), 
				entity.getState(), 
				entity.getZipCode());
	}

}
