package com.whisky.blogrestapi.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.whisky.blogrestapi.payload.CommentDto;
import com.whisky.blogrestapi.service.CommentService;

@RestController
@RequestMapping("/api/")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<CommentDto> createComment(
			@PathVariable long postId,
			@RequestBody @Valid CommentDto commentDto) {

		CommentDto returnCommentDto = commentService.createComment(postId, commentDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(returnCommentDto);

	}

	@GetMapping("/posts/{postId}/comments")
	public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable long postId) {
		List<CommentDto> listOfCommentDto = commentService.getAllCommentByPostId(postId);
		return ResponseEntity.ok(listOfCommentDto);
	}

	@GetMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<CommentDto> getAllCommentsByPostId(
			@PathVariable long postId,
			@PathVariable long commentId) {
		CommentDto returnComment = commentService.getCommentById(postId, commentId);
		return ResponseEntity.ok(returnComment);
	}
	
	@PutMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<CommentDto> getAllCommentsByPostId(
			@PathVariable long postId,
			@PathVariable long commentId,
			@RequestBody @Valid CommentDto commentDto) {
		CommentDto returnComment = commentService.updateCommentById(postId, commentId, commentDto);
		return ResponseEntity.ok(returnComment);
	}
	
	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	public ResponseEntity<String> deleteCommentById(
			@PathVariable long postId,
			@PathVariable long commentId) {
		String returnMessage = commentService.deleteCommentById(postId, commentId);
		return ResponseEntity.ok(returnMessage);
	}
	
	

}
