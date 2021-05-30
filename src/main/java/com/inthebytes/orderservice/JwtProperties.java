package com.inthebytes.orderservice;

public class JwtProperties {
	public static final String SECRET = System.getenv("SL_SECRET");
	public static final int EXPIRATION_TIME = 3600000 * 2; //2 hours
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authentication";
	public static final String AUTHORITIES_KEY = "InBytesAuth";
}
