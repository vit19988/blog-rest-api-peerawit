package com.whisky.blogrestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
	private final String resourceName;
	private final String fieldName;
	private final Long fieldValue;
	
	public ResourceNotFoundException(String resourceName, String fieldName, long id) {
		super(String.format("%s not found with %s : %d",resourceName,fieldName,id));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = id;
	}
	
	
	
}
