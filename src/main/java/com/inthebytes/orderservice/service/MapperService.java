package com.inthebytes.orderservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.inthebytes.orderservice.dto.ItemDto;
import com.inthebytes.orderservice.dto.LocationDto;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;
import com.inthebytes.orderservice.dto.RestaurantDto;
import com.inthebytes.orderservice.dto.UserDto;
import com.inthebytes.orderservice.entity.Location;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.OrderFood;
import com.inthebytes.orderservice.entity.Restaurant;
import com.inthebytes.orderservice.entity.User;

@Service
public class MapperService {
	
	public Order updateOrder(Order order, OrderSubmissionDto data) {
		order.setWindowStart(data.getWindowStart());
		order.setWindowEnd(data.getWindowEnd());
		order.setStatus(data.getStatus());
		if (data.getSpecialInstructions() == null)
			data.setSpecialInstructions("none");
		order.setSpecialInstructions(data.getSpecialInstructions());
		return order;
	}
	
	public Order createOrder(Order order, OrderSubmissionDto data) {
		data.setStatus(0);
		return updateOrder(order, data);
	}
	
	public Location createLoc(@Valid LocationDto data) {
		Location loc = new Location();
		loc.setUnit(data.getUnit());
		loc.setStreet(data.getStreet());
		loc.setCity(data.getCity());
		loc.setState(data.getState());
		loc.setZipCode(data.getZipCode());
		return loc;
	}
	
	public LocationDto convert(Location entity) {
		LocationDto dto = new LocationDto();
		dto.setUnit(entity.getUnit());
		dto.setStreet(entity.getStreet());
		dto.setCity(entity.getCity());
		dto.setState(entity.getState());
		dto.setZipCode(entity.getZipCode());
		return dto;
	}
	
	public Page<OrderDisplayDto> convert(Page<Order> orders) {
		return orders.map((x) -> convert(x));
	}
	
	public OrderDisplayDto convert(Order entity) {
		if (entity == null)
			return null;
		OrderDisplayDto order = new OrderDisplayDto();
		order.setId(entity.getId());
		order.setDestination(convert(entity.getDestination()));
		order.setItems(convert(entity.getFoods()));
		order.setCustomer(convert(entity.getCustomer()));
		if (entity.getDelivery() != null)
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
		item.setName(food.getFood().getName());
		item.setPrice(food.getFood().getPrice().multiply(BigDecimal.valueOf(food.getQuantity())));
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

}
