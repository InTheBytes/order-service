package com.inthebytes.orderservice.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "restaurant")
public class Restaurant implements Serializable {

	private static final long serialVersionUID = 2167268132667674449L;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
	    name = "UUID",
	    strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "restaurant_id")
	private String id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "cuisine")
	private String cuisine;
	
	@ManyToOne @JoinColumn(name = "location_id")
	private Location location;
	
	@OneToMany
	@Nullable
	@JoinTable(name = "manager",
			joinColumns = {@JoinColumn(name = "restaurant_id")},
			inverseJoinColumns = {@JoinColumn(name = "user_id")})
	private List<User> manager;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<User> getManager() {
		return manager;
	}

	public void setManager(List<User> manager) {
		this.manager = manager;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
