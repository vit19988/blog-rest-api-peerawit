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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class LoginController {

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
	public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto logingDto) {
		System.out.println("----------------------");

		Authentication authentication = customUserAuthProvider.authenticate(
				new UsernamePasswordAuthenticationToken(logingDto.getUsernameOrEmail(),
						logingDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// get token from token provide class
		String token = jwtTokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JWTAuthResponse(token));
	}

	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDto) {
		// add check for username exists in a DB
		if (userRepository.existsByEmailOrUsername(signupDto.getEmail(), signupDto.getUsername())) {
			log.warn("------in thi----");
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
