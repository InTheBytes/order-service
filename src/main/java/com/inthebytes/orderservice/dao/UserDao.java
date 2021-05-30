package com.inthebytes.orderservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
