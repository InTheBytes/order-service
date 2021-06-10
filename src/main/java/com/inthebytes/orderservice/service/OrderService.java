package com.inthebytes.orderservice.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.text.html.Option;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.inthebytes.orderservice.dao.FoodDao;
import com.inthebytes.orderservice.dao.LocationDao;
import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.dao.UserDao;
import com.inthebytes.orderservice.dao.RestaurantDao;
import com.inthebytes.orderservice.dto.ItemDto;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;
import com.inthebytes.orderservice.dto.group.AdminSubmissionCheck;
import com.inthebytes.orderservice.dto.group.UserSubmissionCheck;
import com.inthebytes.orderservice.entity.Food;
import com.inthebytes.orderservice.entity.Location;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.OrderFood;
import com.inthebytes.orderservice.entity.OrderFoodKey;
import com.inthebytes.orderservice.entity.Restaurant;
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
	private LocationDao locRepo;

	@Autowired
	private FoodDao foodRepo;

	@Autowired
	private MapperService mapper;

	@Autowired
	private TokenService tokenService;


	/**
	 * GET ORDERS PAGE
	 * @param page
	 * @param pageSize
	 * @param token
	 * @return
	 * @throws NotAuthorizedException
	 */
	public Page<OrderDisplayDto> getOrders(Integer page, Integer pageSize, String token) throws NotAuthorizedException {
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


	/**
	 * GET ORDER
	 * @param orderId
	 * @param token
	 * @return
	 * @throws EntityNotExistsException
	 * @throws NotAuthorizedException
	 */
	public OrderDisplayDto getOrder(String orderId, String token) throws EntityNotExistsException, NotAuthorizedException {
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

	/**
	 * CANCEL ORDER
	 * @param id
	 * @param token
	 * @return
	 * @throws EntityNotExistsException
	 * @throws NotAuthorizedException
	 */
	public Boolean cancelOrder(String id, String token) throws EntityNotExistsException, NotAuthorizedException {
		Optional<Order> order = orderRepo.findById(id);
		if (!order.isPresent())
			throw new EntityNotExistsException("Order with given ID does not exist");

		Credentials account = tokenService.readToken(token);
		switch(account.getRole()) {
		case "ROLE_ADMIN":
			break;
		case "ROLE_CUSTOMER":
			if (order.get().getCustomer().getUsername().equals(account.getUsername()))
				break;
		default:
			throw new NotAuthorizedException("User type not authorized to cancel orders");
		}
		order.get().setStatus(5);
		orderRepo.save(order.get());
		return true;
	}
	
	/**
	 * ORGANIZES UPDATE BY AUTHORIZATION
	 * @param id
	 * @param data
	 * @param token
	 * @return
	 * @throws NotAuthorizedException
	 * @throws EntityNotExistsException
	 * @throws InvalidSubmissionException
	 */
	public OrderDisplayDto updateOrder(String id, OrderSubmissionDto data, String token) 
			throws NotAuthorizedException, EntityNotExistsException, InvalidSubmissionException {
		Credentials account = tokenService.readToken(token);
		Optional<Order> order = orderRepo.findById(id);
		if (order.isEmpty()) {
			throw new EntityNotExistsException();
		}
		switch(account.getRole()) {
		case "ROLE_ADMIN":
			return authorizedUpdateOrder(order.get(), data);
		case "ROLE_CUSTOMER":
			if (account.getUsername().equals(order.get().getCustomer().getUsername())) {
				return authorizedUpdateOrder(order.get(), data);
			} else {
				throw new NotAuthorizedException();
			}
		case "ROLE_DRIVER":
		case "ROLE_RESTAURANT":
			if (data.getStatus() != null)
				return statusUpdate(order.get(), account.getUsername(), data.getStatus());
		default:
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * UPDATES ORDER STATUS WITH AUTHORIZATION
	 * @param entity
	 * @param username
	 * @param status
	 * @return
	 * @throws NotAuthorizedException
	 */
	public OrderDisplayDto statusUpdate(Order entity, String username, Integer status) throws NotAuthorizedException {
		switch(status) {
		case 2:
		case 3:
		case 4:
			if (username.equals(entity.getDriver().getUsername())) {
				break;
			}
			throw new NotAuthorizedException();
		case 5:
			List<String> usernames = entity.getRestaurant().getManager()
				.stream()
				.map((x) -> x.getUsername())
				.collect(Collectors.toList());
			if (usernames.contains(username)) {
				break;
			}
		default:
			throw new NotAuthorizedException();
		}
		entity.setStatus(status);
		return mapper.convert(entity);
	}

	/**
	 * UPDATE ORDER WITH SUBMISSION
	 * @param id
	 * @param data
	 * @param token
	 * @return
	 * @throws EntityNotExistsException 
	 * @throws InvalidSubmissionException 
	 */
	public OrderDisplayDto authorizedUpdateOrder(Order entity, OrderSubmissionDto data) 
			throws EntityNotExistsException, InvalidSubmissionException {
		
		if (data.getItems() != null && data.getItems().size() > 0)
			entity = setFoods(entity, data);
		
		if (data.getWindowStart() == null)
			data.setWindowStart(entity.getWindowStart());
		if (data.getWindowEnd() == null)
			data.setWindowEnd(entity.getWindowEnd());
		
		entity = mapper.updateOrder(entity, data);
		entity = orderRepo.save(entity);
		return mapper.convert(entity);
	}

	/**
	 * CREATE ORDER FROM SUBMISSION
	 * @param data
	 * @param token
	 * @return
	 * @throws NotAuthorizedException
	 * @throws InvalidSubmissionException
	 * @throws EntityNotExistsException
	 */
	public OrderDisplayDto createOrder(OrderSubmissionDto data, String token) throws NotAuthorizedException, InvalidSubmissionException, EntityNotExistsException {
		Order order = new Order();
		data = setTimeWindow(data);
		order = setRestaurant(order, data);
		order = setDestination(order, data);
		order = setFoods(order, data);

		Credentials account = tokenService.readToken(token);
		switch (account.getRole()) {
		case "ROLE_ADMIN":
			return adminCreate(order, data);
		case "ROLE_CUSTOMER":
			return customerCreate(order, data, account.getUsername());
		default:
			throw new NotAuthorizedException("Account not authorized to create orders");
		}
	}

	/**
	 * SET DEFAULT TIME WINDOW
	 * @param data
	 * @return
	 */
	private OrderSubmissionDto setTimeWindow(OrderSubmissionDto data) {
		if (data.getWindowStart() == null) {
			data.setWindowStart(Timestamp.from(Instant.now()));
		} 
		if (data.getWindowEnd() == null) {
			Instant endTime = data.getWindowStart().toInstant().plus(1L, ChronoUnit.HOURS);
			data.setWindowEnd(Timestamp.from(endTime));
		}
		return data;
	}

	/**
	 * SET RESTAURANT
	 * @param order
	 * @param data
	 * @return
	 * @throws InvalidSubmissionException
	 * @throws EntityNotExistsException
	 */
	private Order setRestaurant(Order order, OrderSubmissionDto data) throws InvalidSubmissionException, EntityNotExistsException {
		Optional<Restaurant> restaurant = restaurantRepo.findById(data.getRestaurantId());
		if (restaurant.isPresent()) {
			order.setRestaurant(restaurant.get());
		} else {
			throw new EntityNotExistsException("No restaurant exists with this ID");
		}
		return order;
	}

	/**
	 * SET DESTINATION
	 * @param order
	 * @param data
	 * @return
	 * @throws InvalidSubmissionException
	 */
	private Order setDestination(Order order, OrderSubmissionDto data) throws InvalidSubmissionException {
		if (data.getDestinationId() != null && data.getDestinationId().trim().length() != 0) {
			Optional<Location> loc = locRepo.findById(data.getDestinationId());
			if (loc.isPresent()) {
				order.setDestination(loc.get());
			} else if (data.getDestination() == null) {
				throw new InvalidSubmissionException("A valid location was not provided");
			}
		} else {
			order.setDestination(mapper.createLoc(data.getDestination()));
		}
		return order;
	}

	/**
	 * SET FOODS
	 * @param order
	 * @param data
	 * @return
	 * @throws InvalidSubmissionException
	 */
	private Order setFoods(Order order, OrderSubmissionDto data) throws InvalidSubmissionException {
		List<OrderFood> foods = new ArrayList<OrderFood>();
		data.getItems().stream().forEach(
				(x) -> {
					Optional<Food> food = foodRepo.findById(x.getFood());
					if (food.isPresent()) {
						OrderFood item = new OrderFood();
						item.setFood(food.get());
						item.setOrder(order);
						item.setQuantity(x.getQuantity());
						foods.add(item);
					}
				}
				);
		if (foods.size() > 0) {
			order.setFoods(foods);
		} else {
			throw new InvalidSubmissionException("No existing foods included");
		}
		return order;
	}

	/**
	 * ADMIN CREATE
	 * @param order
	 * @param data
	 * @return
	 * @throws EntityNotExistsException
	 * @throws InvalidSubmissionException
	 */
	private OrderDisplayDto adminCreate(Order order, @Validated(AdminSubmissionCheck.class) OrderSubmissionDto data) 
			throws EntityNotExistsException, InvalidSubmissionException {
		
		if (data.getCustomerId() == null)
			throw new InvalidSubmissionException("A customer account must be assigned to the order");
		else {
			Optional<User> user = userRepo.findById(data.getCustomerId());
			if (user.isPresent())
				order.setCustomer(user.get());
			else {
				throw new EntityNotExistsException("Customer account does not exist");
			}
		}
		order = mapper.createOrder(order, data);
		return mapper.convert(orderRepo.save(order));
	}

	/**
	 * CUSTOMER CREATE
	 * @param order
	 * @param data
	 * @param username
	 * @return
	 */
	private OrderDisplayDto customerCreate(Order order, @Validated(UserSubmissionCheck.class) OrderSubmissionDto data, String username) {
		order.setCustomer(userRepo.findByUsername(username));
		order = mapper.createOrder(order, data);
		return mapper.convert(orderRepo.save(order));
	}
}
