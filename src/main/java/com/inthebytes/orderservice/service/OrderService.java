package com.inthebytes.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.inthebytes.orderservice.JwtProperties;
import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.dao.RestaurantDao;
import com.inthebytes.orderservice.dao.UserDao;
import com.inthebytes.orderservice.dto.OrderDto;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.Restaurant;
import com.inthebytes.orderservice.entity.User;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.TokenService.Credentials;

@Service
public class OrderService {
	
	@Autowired
	MapperService mapper;
	
	@Autowired
	TokenService tokenReader;
	
	@Autowired
	OrderDao orderRepo;
	
	@Autowired
	UserDao userRepo;
	
	@Autowired
	RestaurantDao restaurantRepo;
	
	
	public Page<OrderDto> getOrdersByAuth(String token, Integer pageSize, Integer page) 
			throws NotAuthorizedException, EntityNotExistsException {
		
		Credentials user = tokenReader.readToken(token);
		switch (user.getRole()) {
		case "ROLE_ADMIN":
			return getOrders(pageSize, page);
		case "ROLE_USER":
			return getOrdersByUsername(user.getUsername(), pageSize, page);
		case "ROLE_RESTAURANT":
			return getOrdersByRestaurant(user.getUsername(), pageSize, page);
		case "ROLE_DRIVER":
			return getOrdersByDriver(user, pageSize, page);
		default:
			throw new NotAuthorizedException();
		}
	}
	
	
	public User findUser(String username) throws NotAuthorizedException {
		User user = userRepo.findByUsername(username);
		if (user == null)
			throw new NotAuthorizedException();
		else
			return user;
	}
	
	
	public Page<OrderDto> getOrdersByUsername(String username, Integer pageSize, Integer page) 
			throws NotAuthorizedException {
		
		return mapper.convert(
				orderRepo.findByUserId(
						findUser(username).getUserId(), 
						PageRequest.of(page, pageSize)));
	}
	
	
	public Page<OrderDto> getOrdersByRestaurant(String username, Integer pageSize, Integer page) 
			throws NotAuthorizedException, EntityNotExistsException {
		
		Restaurant restaurant = restaurantRepo.findByRestaurantId(
				findUser(username)
				.getRestaurant()
				.getRestaurantId());
		
		if (restaurant == null)
			throw new EntityNotExistsException();
		return mapper.convert(
				orderRepo.findByRestaurantId(
						restaurant.getRestaurantId(), 
						PageRequest.of(page, pageSize)));
	}
	
	public Page<OrderDto> getOrdersByDriver(Credentials user, Integer pageSize, Integer page) {
		return null;
	}
	
	
	public Page<OrderDto> getOrders(Integer pageSize, Integer page) {
		return mapper.convert(orderRepo.findAll(PageRequest.of(page, pageSize)));
	}
	
	
	public OrderDto getOrder(String token, String orderId) 
			throws EntityNotExistsException, NotAuthorizedException {
		
		Order order = orderRepo.findByOrderId(orderId);
		if (order == null)
			throw new EntityNotExistsException();
		
		Credentials user = tokenReader.readToken(token);
		if ("ROLE_ADMIN".equals(user.getRole()) || 
				user.getUsername().equals(order.getCustomer().getUsername()) ||
				(user.getRestaurant() != null && user.getRestaurant().equals(order.getRestaurant())))
			
			return mapper.convert(orderRepo.findByOrderId(orderId));
		else
			throw new NotAuthorizedException();
	}
	
	
	public OrderDto updateOrder(OrderDto newDetails, String orderId) throws EntityNotExistsException {
		if (orderRepo.findByOrderId(orderId) == null)
			throw new EntityNotExistsException();
		
		Order order = mapper.convert(newDetails);
		order.setOrderId(orderId);
		return mapper.convert(orderRepo.save(order));
	}
	
	
	public OrderDto createOrder(OrderDto order) {
		return mapper.convert(orderRepo.save(mapper.convert(order)));
	}
	
	
	public Boolean deleteOrder(Long orderId) {
		return null;
	}
}
