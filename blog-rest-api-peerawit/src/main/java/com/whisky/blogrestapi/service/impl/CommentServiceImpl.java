package com.whisky.blogrestapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.whisky.blogrestapi.entity.Comment;
import com.whisky.blogrestapi.entity.Post;
import com.whisky.blogrestapi.exception.BlogApiException;
import com.whisky.blogrestapi.exception.ResourceNotFoundException;
import com.whisky.blogrestapi.payload.CommentDto;
import com.whisky.blogrestapi.repository.CommentRepository;
import com.whisky.blogrestapi.repository.PostRepository;
import com.whisky.blogrestapi.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private ModelMapper mapper;

	////////////////////////////////////////////

	private CommentDto mapToDto(Comment comment) {

		return mapper.map(comment, CommentDto.class);

	}

	private Comment mapToEntity(CommentDto commentDto) {

		return mapper.map(commentDto, Comment.class);

	}

	////////////////////////////////////////////

	@Override
	public CommentDto createComment(long postId, CommentDto commentDto) {
		Post loadedPost = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		Comment comment = mapToEntity(commentDto);

		comment.setPost(loadedPost);

		Comment savedComment = commentRepository.save(comment);

		return mapToDto(savedComment);
	}

	@Override
	public List<CommentDto> getAllCommentByPostId(long postId) {
		List<Comment> comments = commentRepository.findAll();
		List<CommentDto> returnComments = comments.stream().map(s -> mapToDto(s))
				.collect(Collectors.toList());
		return returnComments;
	}

	@Override
	public CommentDto getCommentById(long postId, long commentId) {

		Comment loadedComment = loadedCommentFromDB(postId, commentId);

		return mapToDto(loadedComment);
	}

	@Override
	public CommentDto updateCommentById(long postId, long commentId, CommentDto commentRequest) {

		Comment loadedComment = loadedCommentFromDB(postId, commentId);
		BeanUtils.copyProperties(commentRequest, loadedComment, "id");
		Comment updatedComment = commentRepository.save(loadedComment);

		return mapToDto(updatedComment);
	}

	@Override
	public String deleteCommentById(long postId, long commentId) {
		Comment loadedComment = loadedCommentFromDB(postId, commentId);
		commentRepository.delete(loadedComment);
		return "Comment with id : " + commentId + " has been deleted successfully";
	}

	////////////////////

	private Comment loadedCommentFromDB(long postId, long commentId) {

		Post loadedPost = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

		Comment loadedComment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

		if (!loadedComment.getPost().getId().equals(loadedPost.getId())) {
			throw new BlogApiException(HttpStatus.NOT_FOUND, "Comment does not belong to post");
		}

		return loadedComment;

	}
}
