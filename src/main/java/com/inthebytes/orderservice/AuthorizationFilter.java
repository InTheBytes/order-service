package com.inthebytes.orderservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;


public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	private void noTokenCheckForActuator(HttpServletRequest request, HttpServletResponse response) {
		if (!request.getRequestURI().contains("actuator")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		String header = request.getHeader(JwtProperties.HEADER_STRING);
		
		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			noTokenCheckForActuator(request, response);
			return;
		}
		
		try {
			Authentication authentication = getAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (TokenExpiredException e) {
			noTokenCheckForActuator(request, response);
			return;
		}
		
		
		chain.doFilter(request, response);
	}
	
	
	private Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(JwtProperties.HEADER_STRING)
				.replace(JwtProperties.TOKEN_PREFIX, "");
		
		if (token != null) {
			DecodedJWT jwt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
					.build()
					.verify(token);
			
			String role = jwt.getClaim(JwtProperties.AUTHORITIES_KEY)
					.asString();
			
			String userName = jwt.getSubject();
			
			if (role != null && userName != null) {
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				authorities.add(authority);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, null, authorities);
				return auth;
			}
			return null;
		}
		return null;
	}
	
}
