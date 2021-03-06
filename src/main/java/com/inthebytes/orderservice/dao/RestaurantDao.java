package com.inthebytes.orderservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.Restaurant;
import com.inthebytes.orderservice.entity.User;

@Repository
public interface RestaurantDao extends JpaRepository<Restaurant, String>{
	Restaurant findByManager(User manager);
}
