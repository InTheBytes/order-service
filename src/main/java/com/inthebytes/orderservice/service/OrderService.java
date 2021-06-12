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
	private TokenService tokenService;
	
	@Autowired
	private GetOrderService getService;
	
	@Autowired
	private CancelOrderService deleteService;
	
	@Autowired
	private UpdateOrderService updateService;
	
	@Autowired
	private CreateOrderService createService;
	
	/**
	 * GET ORDER BY ID
	 * @param orderId
	 * @param token
	 * @return
	 */
	public OrderDisplayDto getOrder(String orderId, String token) {
		return getService.getOrder(orderId, tokenService.readToken(token));
	}
	
	/**
	 * GET PAGE OF ORDERS
	 * @param page
	 * @param pageSize
	 * @param token
	 * @return
	 */
	public Page<OrderDisplayDto> getOrders(Integer page, Integer pageSize, String token) {
		return getService.getOrders(page, pageSize, tokenService.readToken(token));
	}

	/**
	 * CANCEL ORDER BY ID
	 * @param orderId
	 * @param token
	 * @return
	 */
	public Boolean cancelOrder(String orderId, String token) {
		return deleteService.cancelOrder(orderId, tokenService.readToken(token));
	}
	
	/**
	 * UPDATE ORDER BY ID WITH GIVEN DATA
	 * @param orderId
	 * @param data
	 * @param token
	 * @return
	 */
	public OrderDisplayDto updateOrder(String orderId, OrderSubmissionDto data, String token) {
		return updateService.updateOrder(orderId, data, tokenService.readToken(token));
	}
	
	
	/**
	 * CREATE ORDER FROM GIVEN DATA
	 * @param data
	 * @param token
	 * @return
	 */
	public OrderDisplayDto createOrder(OrderSubmissionDto data, String token) {
		return createService.createOrder(data, tokenService.readToken(token));
	}
}
