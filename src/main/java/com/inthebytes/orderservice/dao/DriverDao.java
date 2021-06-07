package com.inthebytes.orderservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.Driver;

@Repository
public interface DriverDao extends JpaRepository<Driver, String> {
	Driver findByUserId(String userId);
}
