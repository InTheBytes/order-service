package com.inthebytes.orderservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception
    {
        security.csrf().disable() // TODO: REMOVE ON PROD
            .cors()
            
            .and()
            .sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			
			.and()
			.addFilter(new AuthorizationFilter(authenticationManager()))
			.authorizeRequests()
				.antMatchers(HttpMethod.DELETE, "/orders/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET, "/orders/**").authenticated()
				.antMatchers(HttpMethod.GET, "/orders").authenticated()
				.antMatchers(HttpMethod.POST, "/orders").hasAnyRole("ADMIN", "CUSTOMER")
				.antMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("ADMIN", "CUSTOMER")
				.antMatchers(HttpMethod.PUT, "/orders/**").authenticated()
			
			.and()
        	.httpBasic();
    }
    
    @Override
    @Bean
	public AuthenticationManager authenticationManager() throws Exception {
	    return super.authenticationManagerBean();
	}
}