package com.whisky.blogrestapi.service;

import java.util.List;

import com.whisky.blogrestapi.entity.Comment;
import com.whisky.blogrestapi.payload.CommentDto;

public interface CommentService {
	
	CommentDto createComment(long postId, CommentDto commentDto);
	List<CommentDto> getAllCommentByPostId(long postId);
	CommentDto getCommentById(long postId,long commentId);
	CommentDto updateCommentById(long postId,long commentId, CommentDto commentDto);
	String deleteCommentById(long postId,long commentId);

}
