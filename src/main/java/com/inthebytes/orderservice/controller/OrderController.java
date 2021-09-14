package com.inthebytes.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inthebytes.orderservice.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.inthebytes.orderservice.dto.OrderDisplayDto;
import com.inthebytes.orderservice.dto.OrderSubmissionDto;

@RestController
@RequestMapping("/order")
@Tag(name = "orders", description = "The microservice for handling operations with orders")
public class OrderController {

	@Autowired
	private OrderService service;

	@Operation(summary = "Get orders", 
			description = "Get page of orders account is authorized to access, or an order by ID", 
			tags = { "orders" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation - order(s) fetched", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDisplayDto.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = OrderDisplayDto.class)),
			}),
			@ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "Account not allowed access to order", content = @Content)
	})
	@GetMapping(value = {"", "/{orderId}"})
	public ResponseEntity<?> getOrders(
			@PathVariable(required = false) String orderId,
			@RequestAttribute String username,
			@RequestAttribute String role,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "page-size", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "status", required = false, defaultValue = "-1") Integer status,
			@RequestParam(value = "day", required = false, defaultValue = "all") String day
			) {
		
		if (orderId == null || orderId.trim().isEmpty()) {
			if (status != -1 || !"all".equals(day)) {
				return ResponseEntity.ok(service.getOrdersByDetails(page, pageSize, username, role, status, day));
			}
			return ResponseEntity.ok(service.getOrders(page, pageSize, username, role));
		} else {
			return ResponseEntity.ok(service.getOrder(orderId, username, role));
		}
	}

	
	@Operation(summary = "Create order", 
			description = "Create an order by submitting required Request Body - Customers auto-linked, admins need to include", 
			tags = { "orders" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Order created sucessfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDisplayDto.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = OrderDisplayDto.class))
			}),
			@ApiResponse(responseCode = "404", description = "An id provided does not exist in the database", content = @Content),
			@ApiResponse(responseCode = "400", description = "Request body lacks required properties", content = @Content),
			@ApiResponse(responseCode = "403", description = "Account not permitted to create orders", content = @Content)
	})
	@PostMapping(value = "")
	public ResponseEntity<OrderDisplayDto> createOrder(
			@RequestBody OrderSubmissionDto data,
			@RequestAttribute String username,
			@RequestAttribute String role
			) {

		OrderDisplayDto result = service.createOrder(data, username, role);
		HttpHeaders headers = new HttpHeaders();
		headers.set("location", result.getId());
		return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(result);
	}

	
	@Operation(summary = "Update order details", 
			description = "Update details of an order such as item list, status, time window, and destination - "
					+ "Users can update details on their own order - Admins can update details of all orders"
					+ "Drivers and Restaurants can update status of orders relative to their role", 
			tags = { "orders" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Order Updated sucessfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrderDisplayDto.class)),
					@Content(mediaType = "application/xml", schema = @Schema(implementation = OrderDisplayDto.class))
			}),
			@ApiResponse(responseCode = "404", description = "The order, or an id in the request body, does not exist", content = @Content),
			@ApiResponse(responseCode = "400", description = "Request body doesn't have any relavant or required properties", content = @Content),
			@ApiResponse(responseCode = "403", description = "Account not permitted to update this order", content = @Content)
	})
	@PutMapping(value = "/{orderId}")
	public ResponseEntity<?> updateOrder(
			@PathVariable String orderId,
			@RequestAttribute String username,
			@RequestAttribute String role,
			@RequestBody OrderSubmissionDto data
			) {

		return ResponseEntity.ok(service.updateOrder(orderId, data, username, role));
	}

	
	@Operation(summary = "Delete order", 
			description = "Delete operation on provided order - sets status to 5 (Cancelled), but keeps data stored", 
			tags = { "orders" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Order cancelled sucessfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "An order by the provided ID does not exist", content = @Content),
			@ApiResponse(responseCode = "403", description = "Account not permitted delete the order", content = @Content)
	})
	@DeleteMapping(value = "/{orderId}")
	public ResponseEntity<?> cancelOrder(
			@PathVariable String orderId,
			@RequestAttribute String username,
			@RequestAttribute String role
			) {

		service.cancelOrder(orderId, username, role);
		return ResponseEntity.ok(null);
	}

}
