package com.inthebytes.orderservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LocationDto {
	
	@NotNull
	private String address;
	
	@NotNull
	private String city;
	
	@NotNull
	@Size(min = 5, max = 10)
	private String zipCode;

}
