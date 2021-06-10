package com.inthebytes.orderservice.service.crud;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.inthebytes.orderservice.dao.FoodDao;
import com.inthebytes.orderservice.dao.LocationDao;
import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.dao.RestaurantDao;
import com.inthebytes.orderservice.dao.UserDao;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;
import com.inthebytes.orderservice.dto.group.AdminSubmissionCheck;
import com.inthebytes.orderservice.dto.group.UserSubmissionCheck;
import com.inthebytes.orderservice.entity.Food;
import com.inthebytes.orderservice.entity.Location;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.OrderFood;
import com.inthebytes.orderservice.entity.Restaurant;
import com.inthebytes.orderservice.entity.User;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.InvalidSubmissionException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.MapperService;
import com.inthebytes.orderservice.service.TokenService.Credentials;

@Service
public class CreateOrderService {
	
	@Autowired
	private MapperService mapper;
	
	@Autowired
	private OrderDao orderRepo;
	
	@Autowired
	private RestaurantDao restaurantRepo;
	
	@Autowired
	private UserDao userRepo;
	
	@Autowired
	private LocationDao locRepo;
	
	@Autowired
	private FoodDao foodRepo;

	/**
	 * CREATE ORDER FROM SUBMISSION
	 * @param data
	 * @param token
	 * @return
	 * @throws NotAuthorizedException
	 * @throws InvalidSubmissionException
	 * @throws EntityNotExistsException
	 */
	public OrderDisplayDto createOrder(OrderSubmissionDto data, Credentials account) {
		Order order = new Order();
		data = setTimeWindow(data);
		order = setRestaurant(order, data);
		order = setDestination(order, data);
		order = setFoods(order, data);

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
	private Order setRestaurant(Order order, OrderSubmissionDto data) {
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
	private Order setDestination(Order order, OrderSubmissionDto data) {
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
	public Order setFoods(Order order, OrderSubmissionDto data) {
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
	private OrderDisplayDto adminCreate(Order order, @Validated(AdminSubmissionCheck.class) OrderSubmissionDto data) {
		
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
