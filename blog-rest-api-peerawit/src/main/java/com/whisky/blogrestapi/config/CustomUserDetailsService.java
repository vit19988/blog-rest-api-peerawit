package com.whisky.blogrestapi.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.whisky.blogrestapi.entity.Role;
import com.whisky.blogrestapi.entity.User;
import com.whisky.blogrestapi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User loadedUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException(
						"User not found with username or email: " + usernameOrEmail));

		return new org.springframework.security.core.userdetails.User(loadedUser.getEmail(),
				loadedUser.getPassword(), getGrantedAuthorities(loadedUser.getRoles()));
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<Role> roles) {
		return roles.stream().map(Role::getName).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

}
