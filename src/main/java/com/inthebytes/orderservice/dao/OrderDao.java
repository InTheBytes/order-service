package com.inthebytes.orderservice.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {
	Order findByOrderId(String orderId);
	Order findByDeliveryId(String deliveryId);
	
	Page<Order> findAll(Pageable pageable);
	Page<Order> findByCustomer(String userId, Pageable pageable);
	Page<Order> findByRestaurant(String restaurantId, Pageable pageable);
	
}
