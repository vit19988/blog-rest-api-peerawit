package com.whisky.blogrestapi.payload;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UnAuthorizedResponse {
	
	private Date timestamp;
	private String message;
	private String details;

}
