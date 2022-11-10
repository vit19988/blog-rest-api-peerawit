package com.whisky.blogrestapi.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentDto {
	
	@Schema(hidden=true)
	private long id;
	
	//name should not be null or empty
	@NotNull
	@NotEmpty
	private String name;
	
	@Email
	@NotNull
	@NotEmpty
	private String email;
	
	@NotNull
	@NotEmpty
	@Size(min = 10 , message = "body must have at least 10 characters")
	@Schema(example = "string")
	private String body;

}
