package com.inthebytes.orderservice.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.TemporalType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, String> {
	Page<Order> findAll(Pageable pageable);
	Page<Order> findByCustomerUsername(String username, Pageable pageable);
	Page<Order> findByRestaurantManagerUsername(String username, Pageable pageable);
	Page<Order> findByDeliveryDriverDriverUsername(String username, Pageable pageable);
	Page<Order> findByStatus(Integer status, Pageable pageable);
	
//	@Query(value = "SELECT * FROM `order` o "
//			+ "JOIN restaurant r ON o.restaurant_id = r.restaurant_id "
//			+ "JOIN manager m ON r.restaurant_id = m.restaurant_id " 
//			+ "JOIN user u ON m.user_id = u.user_id "
//			+ "WHERE o.status = ?1 "
//			+ "AND u.username = ?2"
//			+ "AND o.window_start < ?3",
//			nativeQuery = true)
//	Page<Order> todaysOpenOrdersByManager(Integer status, String username, Timestamp latestStart, Pageable pageable);
	
	
	// Query for orders by manager and status before specified date. Name is terrible, but the written query is worse
	Page<Order> findByStatusAndRestaurantManagerUsernameAndWindowStartBefore(
			Integer status, 
			String username, 
			Pageable pageable,
			Timestamp windowStart);
	
	// Query for pending orders by manager after today
	Page<Order> findByStatusAndRestaurantManagerUsernameAndWindowStartAfter( 
			Integer status, 
			String username, 
			Pageable pageable,
			Timestamp windowStart);
	

	// Query for orders by manager and status
	Page<Order> findByStatusAndRestaurantManagerUsername(Integer status, String username, Pageable pageable);

	
	// SHORTCUT METHODS! (with sensible names)
	
	default Page<Order> currentOrdersByManagerUsername(String username, Pageable pageable) {
		return findByStatusAndRestaurantManagerUsernameAndWindowStartBefore(
				1, username, pageable,
				Timestamp.valueOf(LocalDate.now(ZoneId.of("UTC")).atStartOfDay().plusDays(1L))
				);
	}
	
	default Page<Order> scheduledOrdersByManagerUsername(String username, Pageable pageable) {
		return findByStatusAndRestaurantManagerUsernameAndWindowStartAfter(
				1, username, pageable,
				Timestamp.valueOf(LocalDate.now(ZoneId.of("UTC")).atStartOfDay().plusDays(1L))
				);
	}
	
	default Page<Order> readyOrdersByManagerUsername(String username, Pageable pageable) {
		return findByStatusAndRestaurantManagerUsername(2, username, pageable);
	}
}
