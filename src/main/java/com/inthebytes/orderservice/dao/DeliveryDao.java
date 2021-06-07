package com.inthebytes.orderservice.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.Delivery;

@Repository
public interface DeliveryDao extends JpaRepository<Delivery, String> {
	Page<Delivery> findByDriverId(String driverId, Pageable pageable);
}
