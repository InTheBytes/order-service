package com.inthebytes.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.inthebytes.orderservice.JwtProperties;
import com.inthebytes.orderservice.dao.UserDao;

@Service
public class TokenService {

	public class Credentials {
		private String username;
		private String role;
		
		@Autowired
		UserDao repo;
		
		public Credentials(String username, String role) {
			this.username = username;
			this.role = role;
		}
		
		public String getUsername() {return this.username;}
		public String getRole() {return this.role;}
	}
	
	public Credentials readToken(String token) {
		DecodedJWT jwt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
				.build()
				.verify(token.replace(JwtProperties.TOKEN_PREFIX, ""));
		return new Credentials(jwt.getSubject(), jwt.getClaim(JwtProperties.AUTHORITIES_KEY).asString());
	}
}
