package com.inthebytes.orderservice.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;

import com.inthebytes.orderservice.dao.LocationDao;
import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.dao.UserDao;
import com.inthebytes.orderservice.dao.RestaurantDao;
import com.inthebytes.orderservice.dto.FoodDao;
import com.inthebytes.orderservice.dto.ItemDto;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;
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
	 * Get Page of orders by authorization token
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
	 * Get Single order by ID
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
	 * Creates an order using submission DTO
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
	 * Checks if time window included in submission DTO - sets default if not
	 * @param data
	 * @return
	 */
	private OrderSubmissionDto setTimeWindow(OrderSubmissionDto data) {
		if (data.getWindowStart() == null) {
//			LocalDateTime startTime = LocalDateTime.now().plusHours(1L).truncatedTo(ChronoUnit.HOURS);
//			Instant now = Instant.now();
			data.setWindowStart(Timestamp.from(Instant.now()));
		} 
		if (data.getWindowEnd() == null) {
			Instant endTime = data.getWindowStart().toInstant().plus(1L, ChronoUnit.HOURS);
			data.setWindowEnd(Timestamp.from(endTime));
		}
		return data;
	}

	/**
	 * Sets the restaurant for a given order
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
	 * Sets the destination for an order - by Location ID provided (priority) OR Location Details
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
	 * Sets food list an order with item data provided
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
	 * Sets order customer to (required) data provided - user ID. Returns fully created order
	 * @param order
	 * @param data
	 * @return
	 * @throws EntityNotExistsException
	 * @throws InvalidSubmissionException
	 */
	private OrderDisplayDto adminCreate(Order order, OrderSubmissionDto data) throws EntityNotExistsException, InvalidSubmissionException {
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
	 * Sets order customer by authorization data. Returns fully created order
	 * @param order
	 * @param data
	 * @param username
	 * @return
	 */
	private OrderDisplayDto customerCreate(Order order, OrderSubmissionDto data, String username) {
		order.setCustomer(userRepo.findByUsername(username));
		order = mapper.createOrder(order, data);
		return mapper.convert(orderRepo.save(order));
	}
}
