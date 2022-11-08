package com.whisky.blogrestapi.controller;

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
import com.whisky.blogrestapi.payload.PostResponse;
import com.whisky.blogrestapi.service.PostService;
import com.whisky.blogrestapi.utils.AppConstants;

@RestController
@RequestMapping("api/posts")
public class PostController {
	
	
	@Autowired
	private PostService postService;
	
	//create a post
	@PostMapping
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postBody){
		PostDto returnPostBody = postService.createPost(postBody);
		return ResponseEntity.status(HttpStatus.CREATED).body(returnPostBody);
	}
	
	//get all posts
	@GetMapping
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
			) {
		PostResponse returnAllPosts = postService.getAllPosts(pageNo,pageSize, sortBy, sortDir);
		return ResponseEntity.ok(returnAllPosts);
	}
	
	//get post by id
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getPostById(@PathVariable long id) {
		PostDto returnPost = postService.getPostById(id);
		return ResponseEntity.ok(returnPost);
	}
	
	//update post
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePostByid(@PathVariable long id,@Valid @RequestBody PostDto postBody ){
		PostDto returnPost = postService.updatePostById(postBody, id);
		return ResponseEntity.ok(returnPost);
	}
	
	//delete post by id
	@DeleteMapping("{id}")
	public ResponseEntity<String> deletePostById(@PathVariable long id){
		String returnSentence = postService.deletePostById(id);
		return ResponseEntity.ok(returnSentence);
	}

}
