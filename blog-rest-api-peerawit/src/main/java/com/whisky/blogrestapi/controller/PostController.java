package com.whisky.blogrestapi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.whisky.blogrestapi.payload.PostDto;
import com.whisky.blogrestapi.payload.PostDtoV2;
import com.whisky.blogrestapi.payload.PostResponse;
import com.whisky.blogrestapi.service.PostService;
import com.whisky.blogrestapi.utils.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping
@Tag(name = "Post Controller")
public class PostController {

	@Autowired
	private PostService postService;

	// create a post
	@PostMapping("/api/v1/posts")
	@Operation(summary = "Create Post")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postBody) {
		PostDto returnPostBody = postService.createPost(postBody);
		return ResponseEntity.status(HttpStatus.CREATED).body(returnPostBody);
	}

	// get all posts
	@GetMapping("/api/v1/posts")
	@Operation(summary = "Get All Posts")
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
		PostResponse returnAllPosts = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(returnAllPosts);
	}


	// get post by id
	@GetMapping(value="/api/v1/posts/{id}")
	@Operation(summary = "Get Post By Id V.1")
	public ResponseEntity<PostDto> getPostByIdV1(@PathVariable long id) {
		PostDto returnPost = postService.getPostById(id);
		return ResponseEntity.ok(returnPost);
	}
	

	// get post by id
	@GetMapping(value="/api/v2/posts/{id}")
	@Operation(summary = "Get Post By Id V.2")
	public ResponseEntity<PostDtoV2> getPostByIdV2(@PathVariable long id) {
		PostDto postDto= postService.getPostById(id);
		PostDtoV2 postDtoV2 = new PostDtoV2();
		postDtoV2.setId(postDto.getId());
		postDtoV2.setTitle(postDto.getTitle());
		postDtoV2.setDescription(postDto.getDescription());
		postDtoV2.setContent(postDto.getContent());
		
		List<String> tags= new ArrayList<>();
		tags.add("Java");
		tags.add("Spring Boot");
		tags.add("AWS");
		postDtoV2.setTags(tags);
		return ResponseEntity.ok(postDtoV2);
	}

	// update post
	@PutMapping("/api/v1/posts")
	@Operation(summary = "Update Post By Id")
	public ResponseEntity<PostDto> updatePostByid(@PathVariable long id,
			@Valid @RequestBody PostDto postBody) {
		PostDto returnPost = postService.updatePostById(postBody, id);
		return ResponseEntity.ok(returnPost);
	}

	// delete post by id
	@DeleteMapping("/api/v1/posts")
	@Operation(summary = "Delete Post By Id")
	public ResponseEntity<String> deletePostById(@PathVariable long id) {
		String returnSentence = postService.deletePostById(id);
		return ResponseEntity.ok(returnSentence);
	}

}
