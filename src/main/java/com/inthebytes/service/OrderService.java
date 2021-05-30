package com.inthebytes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inthebytes.dao.OrderDao;

@Service
public class OrderService {
	
	@Autowired
	OrderMapper mapper;
	
	@Autowired
	OrderDao repo;

}
