package com.inthebytes.orderservice.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, String> {
	Page<Order> findAll(Pageable pageable);
	Page<Order> findByCustomerUsername(String username, Pageable pageable);
	Page<Order> findByRestaurantManagerUsername(String username, Pageable pageable);
	Page<Order> findByDeliveryDriverDriverUsername(String username, Pageable pageable);
	Page<Order> findByStatus(Integer status, Pageable pageable);
}
