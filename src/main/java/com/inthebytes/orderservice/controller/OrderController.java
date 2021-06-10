package com.inthebytes.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.inthebytes.orderservice.service.OrderService;
import com.inthebytes.orderservice.JwtProperties;
import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;
import com.inthebytes.orderservice.exception.EntityNotExistsException;
import com.inthebytes.orderservice.exception.InvalidSubmissionException;
import com.inthebytes.orderservice.exception.NotAuthorizedException;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService service;

	@GetMapping(value = {"", "/{orderId}"})
	public ResponseEntity<?> getOrders(
			@PathVariable(required = false) String orderId,
			@RequestHeader(name = JwtProperties.HEADER_STRING) String token,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
			) throws NotAuthorizedException, EntityNotExistsException{
		
		if (orderId == null || orderId.trim().isEmpty()) {
			return ResponseEntity.ok(service.getOrders(page, pageSize, token));
		} else {
			return ResponseEntity.ok(service.getOrder(orderId, token));
		}
	}

	@PostMapping(value = "")
	public ResponseEntity<?> createOrder(
			@RequestBody OrderSubmissionDto data,
			@RequestHeader(name = JwtProperties.HEADER_STRING) String token
			) throws NotAuthorizedException, InvalidSubmissionException, EntityNotExistsException {

		return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(data, token));
	}

	@PutMapping(value = "/{orderId}")
	public ResponseEntity<?> updateOrder(
			@PathVariable String orderId,
			@RequestHeader(name = JwtProperties.HEADER_STRING) String token,
			@RequestBody OrderSubmissionDto data
			) throws EntityNotExistsException, InvalidSubmissionException, NotAuthorizedException {

		return ResponseEntity.ok(service.updateOrder(orderId, data, token));
	}

	@DeleteMapping(value = "/{orderId}")
	public ResponseEntity<?> cancelOrder(
			@PathVariable String orderId,
			@RequestHeader(name = JwtProperties.HEADER_STRING) String token
			) throws EntityNotExistsException, NotAuthorizedException {

		service.cancelOrder(orderId, token);
		return ResponseEntity.ok(null);
	}

}
