package com.inthebytes.orderservice.service.crud;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.TokenService.Credentials;

@Service
public class CancelOrderService {

	@Autowired
	private OrderDao orderRepo;
	
	/**
	 * CANCEL ORDER
	 * @param id
	 * @param token
	 * @return
	 * @throws EntityNotExistsException
	 * @throws NotAuthorizedException
	 */
	public Boolean cancelOrder(String id, Credentials account) {
		Optional<Order> order = orderRepo.findById(id);
		if (!order.isPresent())
			throw new EntityNotExistsException("Order with given ID does not exist");

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
}
