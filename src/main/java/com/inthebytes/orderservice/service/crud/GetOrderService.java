package com.inthebytes.orderservice.service.crud;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.InvalidSubmissionException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.MapperService;

@Service
@Transactional
public class GetOrderService {
	
	@Autowired
	private MapperService mapper;
	
	@Autowired
	private OrderDao orderRepo;

	/**
	 * Get orders page (sorted by recency) for relevant orders.
	 * Admin gets page from all orders, Managers from orders to their restaurant,
	 * Drivers for orders they delivered, and customers for orders they placed.
	 * @param page
	 * @param pageSize
	 * @param username
	 * @param role
	 * @return OrderDisplayDto
	 * @throws NotAuthorizedException
	 */
	public Page<OrderDisplayDto> getOrders(Integer page, Integer pageSize, String username, String role) {
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("windowStart").descending());
		
		Page<Order> orders;
		switch (role) {
		case "admin":
			orders = orderRepo.findAll(pageable);
			break;
		case "customer":
			orders = orderRepo.findByCustomerUsername(username, pageable);
			break;
		case "driver":
			orders = orderRepo.findByStatus(0, pageable);
//			orders = orderRepo.findByDeliveryDriverDriverUsername(username, pageable);
			break;
		case "restaurant":
			orders = orderRepo.findByRestaurantManagerUsername(username, pageable);
			break;
		default:
			throw new NotAuthorizedException("Invalid Authorization Token");
		}
		return mapper.convert(orders);
	}
	
	/**
	 * Gets an order by ID without checking relevance. ADMIN ONLY
	 * @param orderId
	 * @return OrderDisplayDto
	 */
	public OrderDisplayDto getOrder(String orderId) {
		return mapper.convert(orderRepo.getById(orderId));
	}

	/**
	 * Get order if the provided username matches the customer, driver, or one of the restaurant's managers
	 * @param orderId
	 * @param username
	 * @return OrderDisplayDto
	 * @throws EntityNotExistsException
	 * @throws NotAuthorizedException
	 */
	public OrderDisplayDto getOrder(String orderId, String username) {
		Optional<Order> order = orderRepo.findById(orderId);
		if (!order.isPresent())
			throw new EntityNotExistsException("No order exists by this ID");
		if (order.get().getCustomer().getUsername().equals(username) ||
				order.get().getDriver().getUsername().equals(username)
				) {
			return mapper.convert(order.get());
		} else {
			try {
				List<String> managers = order.get().getRestaurant().getManager()
						.stream()
						.map((x) -> x.getUsername())
						.collect(Collectors.toList());
				if (managers.contains(username)) {
					return mapper.convert(order.get());
				}
			} catch (NullPointerException e) {
				throw new EntityNotExistsException("The associated restaurant doesn't have managers, or doesn't exist");
			}
			throw new NotAuthorizedException("Not authorized to view this order");
		}
	}
	
	/**
	 * Big ol' method for getting specific kinds of orders for a restaurant account managing orders.
	 * This is mainly intended for tracking and fulfilling orders for the 'kitchen staff' using the app.
	 * Thus, implementation is EXTREMELY limited, picky, and poorly designed.
	 * @param page
	 * @param pageSize
	 * @param username
	 * @param role
	 * @param status
	 * @param day
	 * @return
	 */
	public Page<OrderDisplayDto> getOrdersWithDetails(
			Integer page, 
			Integer pageSize, 
			String username, 
			String role, 
			Integer status, 
			String day) {
		
		if (!"restaurant".equals(role)) {
			throw new NotAuthorizedException(
					"Parameters for day and status are currently only supported for restaurant manager accounts");
		} 
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "windowStart"));
		Function<Order, OrderDisplayDto> mapping = x -> mapper.convert(x); 
		
		if (status == 2) {
			return orderRepo.readyOrdersByManagerUsername(username, pageable).map(mapping);
		} 
		
		if (status <= 1){
			if ("today".equals(day)) {
				return orderRepo.currentOrdersByManagerUsername(username, pageable).map(mapping);
			} else if ("future".equals(day)) {
				return orderRepo.scheduledOrdersByManagerUsername(username, pageable).map(mapping);
			} else {
				return orderRepo.findByStatusAndRestaurantManagerUsername(status, username, pageable).map(mapping);
			}
		}
		
		if (status > 2 && !"all".equals(day)) {
			throw new InvalidSubmissionException(String.format("This API does not yet support the configuration of status %d with day parameter: %s", status, day));
		} else if (status > 2) {
				return orderRepo.findByStatusAndRestaurantManagerUsername(status, username, pageable).map(mapping);
		} else {
				return orderRepo.findByRestaurantManagerUsername(username, pageable).map(mapping);
		}
	}

}
