package com.inthebytes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import com.inthebytes.dto.OrderDto;
import com.inthebytes.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping(value = {"", "/{orderId}"})
	public ResponseEntity<Page<OrderDto>> getOrders(
			@PathVariable(required = false) String orderId,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize, 
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page
			) {
		
		if (orderId != null) {
			
		} else {
			
		}
		
		return null;
	}
	
	@PutMapping(value = {"", "/{orderId}"})
	public ResponseEntity<OrderDto> updateOrder(
			@PathVariable(required = false) String orderId,
			@Valid @RequestBody OrderDto updatedOrder
			) {
		
		if (orderId != null) {
			
		} else {
			
		}
		
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