package com.whisky.blogrestapi.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whisky.blogrestapi.entity.Role;
import com.whisky.blogrestapi.entity.User;
import com.whisky.blogrestapi.payload.JWTAuthResponse;
import com.whisky.blogrestapi.payload.LoginDto;
import com.whisky.blogrestapi.payload.SignupDto;
import com.whisky.blogrestapi.repository.RoleRepository;
import com.whisky.blogrestapi.repository.UserRepository;
import com.whisky.blogrestapi.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authorization Controller")
public class AuthController {

	@Autowired
	private AuthenticationProvider customUserAuthProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@PostMapping("/signin")
	@Operation(summary = "REST API to Login")
	public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto logingDto) {
		
		Authentication authentication = customUserAuthProvider.authenticate(
				new UsernamePasswordAuthenticationToken(logingDto.getUsernameOrEmail(),
						logingDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// get token from token provide class
		String token = jwtTokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JWTAuthResponse(token));
	}

	@PostMapping("/signup")
	@Operation(summary = "REST API to Register")
	public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDto) {
		// add check for username exists in a DB
		if (userRepository.existsByEmailOrUsername(signupDto.getEmail(), signupDto.getUsername())) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Username or email is already taken!");
		}

		// create user object
		User user = new User();
		user.setName(signupDto.getName());
		user.setUsername(signupDto.getUsername());
		user.setEmail(signupDto.getEmail());
		user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

		Role roles = roleRepository.findByName("ROLE_USER").get();
		user.setRoles(Collections.singletonList(roles));

		userRepository.save(user);

		return ResponseEntity.ok("User registered successfully!");
	}

}
