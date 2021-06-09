package com.inthebytes.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inthebytes.orderservice.service.OrderService;
import com.inthebytes.orderservice.JwtProperties;

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
			) throws Exception {
		if (orderId == null || orderId.trim().isEmpty()) {
			return ResponseEntity.ok(service.getOrders(page, pageSize, token));
		} else {
			return ResponseEntity.ok(service.getOrder(orderId, token));
		}
	}

}
