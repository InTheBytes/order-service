package com.inthebytes.orderservice.service.crud;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;
import com.inthebytes.orderservice.entity.Order;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.InvalidSubmissionException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.TokenService.Credentials;
import com.inthebytes.orderservice.dao.OrderDao;
import com.inthebytes.orderservice.service.crud.UpdateOrderService;
import com.inthebytes.orderservice.service.MapperService;

@Service
public class UpdateOrderService {
	
	@Autowired
	private MapperService mapper;
	
	@Autowired
	private OrderDao orderRepo;
	
	@Autowired
	private CreateOrderService createService;

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
	public OrderDisplayDto updateOrder(String id, OrderSubmissionDto data, Credentials account) {
		Optional<Order> order = orderRepo.findById(id);
		if (!order.isPresent()) {
			throw new EntityNotExistsException("No order by that ID");
		}
		switch(account.getRole()) {
		case "ROLE_ADMIN":
			return authorizedUpdateOrder(order.get(), data);
		case "ROLE_CUSTOMER":
			if (account.getUsername().equals(order.get().getCustomer().getUsername())) {
				return authorizedUpdateOrder(order.get(), data);
			} else {
				throw new NotAuthorizedException("Not authorized to access this order");
			}
		case "ROLE_DRIVER":
		case "ROLE_RESTAURANT":
			if (data.getStatus() != null)
				return statusUpdate(order.get(), account.getUsername(), data.getStatus());
		default:
			throw new NotAuthorizedException("Not authorized to access this order");
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
			throw new NotAuthorizedException("Not authorized for this update");
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
			entity = createService.setFoods(entity, data);
		
		if (data.getWindowStart() == null)
			data.setWindowStart(entity.getWindowStart());
		if (data.getWindowEnd() == null)
			data.setWindowEnd(entity.getWindowEnd());
		
		entity = mapper.updateOrder(entity, data);
		entity = orderRepo.save(entity);
		return mapper.convert(entity);
	}
}
