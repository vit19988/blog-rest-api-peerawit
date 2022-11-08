package com.whisky.blogrestapi.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.whisky.blogrestapi.filter.JwtAuthenticationFilter;
import com.whisky.blogrestapi.security.JwtAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {
	
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter; 

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/api/post/**","/api/comment/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT,"/api/post/**","/api/comment/**").hasRole("ADMIN")
		.antMatchers(HttpMethod.DELETE,"/api/post/**","/api/comment/**").hasRole("ADMIN")
		.antMatchers("/api/auth/**").permitAll()
		.anyRequest().authenticated()
		.and().exceptionHandling()
		.authenticationEntryPoint(authenticationEntryPoint);

		return http.build();

	}

//	@Bean
//	public InMemoryUserDetailsManager userDetailsManager() {
//
//		UserDetails admin = new User("peerawit", "rosebud", getGrantedAuthorities("ROLE_ADMIN"));
//		UserDetails user = new User("user1", "1234", getGrantedAuthorities("ROLE_USER"));
//
//		return new InMemoryUserDetailsManager(admin,user);
//
//	}


	

	private List<GrantedAuthority> getGrantedAuthorities(String role) {
		GrantedAuthority authority = new SimpleGrantedAuthority(role);
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(authority);
		return authorities;
	}

	
}
