package com.whisky.blogrestapi.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.whisky.blogrestapi.entity.Role;
import com.whisky.blogrestapi.entity.User;
import com.whisky.blogrestapi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String usernameOrEmail = authentication.getName();
		String pwd = authentication.getCredentials().toString();

		User loadedUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException(
						"User not found with username or email: " + usernameOrEmail));

		if (!encoder.matches(pwd, loadedUser.getPassword())) {
			throw new BadCredentialsException("Invalid Password");
		}

		log.warn("Success login!");

		return new UsernamePasswordAuthenticationToken(loadedUser.getName(), pwd,
				getGrantedAuthorities(loadedUser.getRoles()));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
		return roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

}
