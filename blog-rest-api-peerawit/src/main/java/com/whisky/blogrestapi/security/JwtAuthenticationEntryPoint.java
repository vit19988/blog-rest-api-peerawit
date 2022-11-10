package com.whisky.blogrestapi.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.whisky.blogrestapi.payload.UnAuthorizedResponse;

import lombok.extern.slf4j.Slf4j;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private UnAuthorizedResponse unAuthorizedResponse;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		
		ObjectWriter ow = new ObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(unAuthorizedResponse);
		
		response.setContentType(MediaType.APPLICATION_JSON.toString());
		response.getWriter().write(json);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

}
