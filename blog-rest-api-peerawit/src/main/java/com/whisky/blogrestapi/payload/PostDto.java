package com.whisky.blogrestapi.payload;

import java.util.List;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class PostDto {
	
	@Schema(hidden=true)
	private Long id;
	
	//title should not be null or empty
	//title should have at least 2 characters
	@NotEmpty
	@NotNull
	@Size(min = 2 ,message = "Post title should have at least 2 characters")
	private String title;
	
	
	//post description should not be null or empty
	//post description should have at least 10 chatacters
	@NotEmpty
	@NotNull
	@Size(min = 10 ,message = "Post description should have at least 10 characters")
	@Schema(example = "string")
	private String description;
	
	
	//post content should not be null or empty
	@NotEmpty
	private String content;
	
	@Schema(hidden=true)
	private List<CommentDto> comments;

}
