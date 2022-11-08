package com.whisky.blogrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BlogApiException extends RuntimeException {
	
	private HttpStatus status;
	private String message;
	
	
	public BlogApiException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
	

}
