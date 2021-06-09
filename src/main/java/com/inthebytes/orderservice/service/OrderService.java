package com.inthebytes.orderservice.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.dao.UserDao;
import com.inthebytes.orderservice.dao.RestaurantDao;
import com.inthebytes.orderservice.dto.OrderDto;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.User;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.InvalidSubmissionException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.TokenService.Credentials;

@Service
public class OrderService {
	
	@Autowired
	private OrderDao orderRepo;
	
	@Autowired
	private UserDao userRepo;
	
	@Autowired
	private RestaurantDao restaurantRepo;
	
	@Autowired
	private MapperService mapper;
	
	@Autowired
	private TokenService tokenService;
	
	
	public Page<OrderDto> getOrders(Integer page, Integer pageSize, String token) throws NotAuthorizedException {
		Pageable pageable = PageRequest.of(page, pageSize);
		Credentials account = tokenService.readToken(token);
		Page<Order> orders;
		switch (account.getRole()) {
		case "ROLE_ADMIN":
			orders = orderRepo.findAll(pageable);
			break;
		case "ROLE_CUSTOMER":
			orders = orderRepo.findByCustomerUsername(account.getUsername(), pageable);
			break;
		case "ROLE_DRIVER":
			orders = orderRepo.findByDeliveryDriverDriverUsername(account.getUsername(), pageable);
			break;
		case "ROLE_RESTAURANT":
			orders = orderRepo.findByRestaurantManagerUsername(account.getUsername(), pageable);
			break;
		default:
			throw new NotAuthorizedException("Invalid Authorization Token");
		}
		return mapper.convert(orders);
	}
	

	public OrderDto getOrder(String orderId, String token) throws EntityNotExistsException, NotAuthorizedException {
		Optional<Order> order = orderRepo.findById(orderId);
		if (!order.isPresent())
			throw new EntityNotExistsException("No order exists by this ID");
		
		Credentials account = tokenService.readToken(token);
		String errorMessage;
		switch (account.getRole()) {
		case "ROLE_ADMIN":
			break;
		case "ROLE_CUSTOMER":
			if (order.get().getCustomer().getUsername().equals(account.getUsername())) {
				break;
			}
		case "ROLE_DRIVER":
			if (order.get().getDriver().getUsername().equals(account.getUsername())) {
				break;
			}
		case "ROLE_RESTAURANT":
			for (User manager : order.get().getRestaurant().getManager()) {
				if (account.getUsername().equals(manager.getUsername()))
					break;
			}
			errorMessage = "Account is not involved with this order";
		default:
			errorMessage = "Invalid authorization token";
			throw new NotAuthorizedException(errorMessage);
		}
		return mapper.convert(order.get());
	}
	
	public OrderDto createOrder(OrderDto data, String token) throws NotAuthorizedException, InvalidSubmissionException, EntityNotExistsException {
		Order order = new Order();
		
		// DEFAULT TIMES
		if (data.getWindowStartTime() == null) {
			LocalDateTime startTime = LocalDateTime.now().plusHours(1L).truncatedTo(ChronoUnit.HOURS);
			data.setWindowStartTime(Timestamp.from(Instant.from(startTime)));
		} 
		if (data.getWindowEndTime() == null) {
			Instant endTime = data.getWindowStartTime().toInstant().plus(1L, ChronoUnit.HOURS);
			data.setWindowEndTime(Timestamp.from(endTime));
		}
		
		// ASSIGN TO CUSTOMER
		Credentials account = tokenService.readToken(token);
		switch (account.getRole()) {
		
		case "ROLE_ADMIN":
			if (data.getCustomer() == null)
				throw new InvalidSubmissionException("A customer account must be assigned to the order");
			else {
				Optional<User> user = userRepo.findById(data.getCustomer().getId());
				if (user.isPresent())
					order.setCustomer(user.get());
				else {
					throw new EntityNotExistsException("Customer account does not exist");
				}
			}
			break;
		case "ROLE_CUSTOMER":
			order.setCustomer(userRepo.findByUsername(account.getUsername()));
			break;
		default:
			throw new NotAuthorizedException("Account not authorized to create orders");
		}
		
		
		return mapper.convert(orderRepo.save(order));
//		private String destination;
//		private UserDto driver;
//		private RestaurantDto restaurant;
//		private List<ItemDto> items;
	}
}
