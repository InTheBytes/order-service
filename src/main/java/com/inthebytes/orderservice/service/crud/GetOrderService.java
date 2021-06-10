package com.inthebytes.orderservice.service.crud;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.entity.User;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.MapperService;
import com.inthebytes.orderservice.service.TokenService.Credentials;

@Service
public class GetOrderService {
	
	@Autowired
	private MapperService mapper;
	
	@Autowired
	private OrderDao orderRepo;

	/**
	 * GET ORDERS PAGE
	 * @param page
	 * @param pageSize
	 * @param token
	 * @return
	 * @throws NotAuthorizedException
	 */
	public Page<OrderDisplayDto> getOrders(Integer page, Integer pageSize, Credentials account) {
		Pageable pageable = PageRequest.of(page, pageSize);
		
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
	public OrderDisplayDto getOrder(String orderId, Credentials account) {
		Optional<Order> order = orderRepo.findById(orderId);
		if (!order.isPresent())
			throw new EntityNotExistsException("No order exists by this ID");

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

}
