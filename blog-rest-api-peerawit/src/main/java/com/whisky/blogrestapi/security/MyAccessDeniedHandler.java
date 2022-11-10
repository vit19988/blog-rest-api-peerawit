package com.whisky.blogrestapi.security;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.whisky.blogrestapi.payload.UnAuthorizedResponse;
import com.whisky.blogrestapi.utils.AuthResponseUtil;

import io.jsonwebtoken.io.IOException;


@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	
	@Autowired
	private UnAuthorizedResponse unAuthorizedResponse;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException)
			throws IOException, ServletException, java.io.IOException {
		
		AuthResponseUtil.setResponseDataToUnAuthorizationBean("Forbidden", request, unAuthorizedResponse);
		AuthResponseUtil.sendResponse(unAuthorizedResponse, response, HttpStatus.FORBIDDEN);
	}

}