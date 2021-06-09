package com.inthebytes.orderservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "driver")
public class Driver implements Serializable {

	private static final long serialVersionUID = 1130772501888811043L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
	    name = "UUID",
	    strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "driver_id")
	private String id;
	
	@ManyToOne @JoinColumn(name = "user_id")
	private User driver;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
