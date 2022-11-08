package com.whisky.blogrestapi.payload;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UnAuthorizedResponse {
	
	private Date timestamp;
	private String message;
	private String details;

}
