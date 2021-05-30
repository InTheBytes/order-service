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
import com.inthebytes.orderservice.dao.UserDao;
import com.inthebytes.orderservice.dto.OrderDto;
import com.inthebytes.orderservice.entity.Order;

@Service
public class OrderService {
	
	@Autowired
	MapperService mapper;
	
	@Autowired
	OrderDao orderRepo;
	
	@Autowired
	UserDao userRepo;
	
	public Page<OrderDto> getOrdersByAuth(String token, Integer pageSize, Integer page) {
		DecodedJWT jwt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
				.build()
				.verify(token.replace(JwtProperties.TOKEN_PREFIX, ""));
		
		switch (jwt.getClaim(JwtProperties.AUTHORITIES_KEY)
				.asString()) {
		case "ROLE_ADMIN":
			return getOrders(pageSize, page);
		case "ROLE_USER":
			return getOrders(jwt.getSubject(), pageSize, page);
		default:
			return null;
		}
	}
	
	private Page<OrderDto> getOrders(String username, Integer pageSize, Integer page) {
		return mapper.convert(
				orderRepo.findByUserId(
						userRepo.findByUsername(username).getUserId(), 
						PageRequest.of(page, pageSize)
						));
	}
	
	private Page<OrderDto> getOrders(Integer pageSize, Integer page) {
		return mapper.convert(orderRepo.findAll(PageRequest.of(page, pageSize)));
	}
	
	public OrderDto getOrder(Long orderId) {
		return mapper.convert(orderRepo.findByOrderId(orderId));
	}
	
	public OrderDto updateOrder(OrderDto newDetails, Long orderId) {
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
