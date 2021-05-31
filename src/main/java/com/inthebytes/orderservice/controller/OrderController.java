package com.inthebytes.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import com.inthebytes.orderservice.JwtProperties;
import com.inthebytes.orderservice.dto.OrderDto;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;
import com.inthebytes.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping(value = {"", "/{orderId}"})
	public ResponseEntity<?> getOrders(
			@PathVariable(required = false) String orderId,
			@RequestHeader(name = JwtProperties.HEADER_STRING) String token,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize, 
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page
			) {
		
		try {
			if (orderId == null) {
				return ResponseEntity.ok().body(orderService.getOrdersByAuth(token, pageSize, page));
			} else {
				return ResponseEntity.ok().body(orderService.getOrder(token, orderId));
			}
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
		} catch (EntityNotExistsException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@PutMapping(value = "/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(
			@Valid @RequestBody OrderDto updatedOrder
			) {
		
		return null;
	}
	
	@PostMapping(value = "")
	public ResponseEntity<OrderDto> createOrder(
			@Valid @RequestBody OrderDto newOrder
			) {
		
		return null;
	}
	

	
	@DeleteMapping(value = "/{orderId}")
	public ResponseEntity<?> deleteOrder(
			@PathVariable("orderId") String orderId
			) {
		
		return null;
	}

}