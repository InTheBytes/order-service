package com.inthebytes.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;
import com.inthebytes.orderservice.service.crud.GetOrderService;
import com.inthebytes.orderservice.service.crud.CancelOrderService;
import com.inthebytes.orderservice.service.crud.UpdateOrderService;
import com.inthebytes.orderservice.service.crud.CreateOrderService;

@Service
public class OrderService {
	
	@Autowired
	private GetOrderService getService;
	
	@Autowired
	private CancelOrderService deleteService;
	
	@Autowired
	private UpdateOrderService updateService;
	
	@Autowired
	private CreateOrderService createService;
	
	/**
	 * Get order by ID - supplying username and role for security checks
	 * @param orderId
	 * @param username
	 * @param role
	 * @return OrderDisplayDto
	 */
	public OrderDisplayDto getOrder(String orderId, String username, String role) {
		return ("admin".equals(role)) ? getService.getOrder(orderId) : getService.getOrder(orderId, username);
	}
	
	/**
	 * GET PAGE OF ORDERS
	 * @param page
	 * @param pageSize
	 * @param username
	 * @param role
	 * @return OrderDisplayDto
	 */
	public Page<OrderDisplayDto> getOrders(Integer page, Integer pageSize, String username, String role) {
		return getService.getOrders(page, pageSize, username, role);
	}
	


	public Page<OrderDisplayDto> getOrdersByDetails(
			Integer page, 
			Integer pageSize, 
			String username, 
			String role, 
			Integer status, 
			String day) {
		
		return getService.getOrdersWithDetails(page, pageSize, username, role, status, day);
	}

	/**
	 * CANCEL ORDER BY ID
	 * @param orderId
	 * @param token
	 * @return
	 */
	public Boolean cancelOrder(String orderId, String username, String role) {
		return deleteService.cancelOrder(orderId, username, role);
	}
	
	/**
	 * UPDATE ORDER BY ID WITH GIVEN DATA
	 * @param orderId
	 * @param data
	 * @param token
	 * @return
	 */
	public OrderDisplayDto updateOrder(String orderId, OrderSubmissionDto data, String username, String role) {
		return updateService.updateOrder(orderId, data, username, role);
	}
	
	
	/**
	 * CREATE ORDER FROM GIVEN DATA
	 * @param data
	 * @param token
	 * @return
	 */
	public OrderDisplayDto createOrder(OrderSubmissionDto data, String username, String role) {
		return createService.createOrder(data, username, role);
	}
}
