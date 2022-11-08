package com.whisky.blogrestapi.payload;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ErrorDetails {
	private Date timestamp;
	private String messsage;
	private String details;
	

}
