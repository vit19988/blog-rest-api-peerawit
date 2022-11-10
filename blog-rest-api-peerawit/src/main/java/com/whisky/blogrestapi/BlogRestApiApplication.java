package com.whisky.blogrestapi;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.whisky.blogrestapi.entity.Role;
import com.whisky.blogrestapi.repository.RoleRepository;

@SpringBootApplication
@EnableWebSecurity(debug = true)
public class BlogRestApiApplication implements CommandLineRunner{
	
	@Autowired
	private RoleRepository roleRepository;
	
	

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Override
	public void run(String... args) throws Exception {
		Role role1 = new Role(1,"ROLE_ADMIN");
		Role role2 = new Role(2,"ROLE_USER");
		roleRepository.save(role1);
		roleRepository.save(role2);

	}
	
	public static void main(String[] args) {
		SpringApplication.run(BlogRestApiApplication.class, args);
		
	
	}

}
