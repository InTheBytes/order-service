package com.inthebytes.orderservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inthebytes.orderservice.entity.Location;

@Repository
public interface LocationDao extends JpaRepository<Location, String>{

}
