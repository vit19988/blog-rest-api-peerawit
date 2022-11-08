package com.whisky.blogrestapi.service;

import java.sql.SQLException;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import com.whisky.blogrestapi.payload.PostDto;
import com.whisky.blogrestapi.payload.PostResponse;

public interface PostService {
	
	PostDto getPostById(long id);
	PostDto createPost(PostDto postDto);
	PostDto updatePostById(PostDto postDto, long id);
	String deletePostById(long id);
	PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
	
}
