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
				.antMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.PUT, "/orders/**").hasAnyRole("ADMIN", "USER")
			
			.and()
        	.httpBasic().disable();
    }
    
//    @Override
//    @Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//	    return super.authenticationManagerBean();
//	}
}