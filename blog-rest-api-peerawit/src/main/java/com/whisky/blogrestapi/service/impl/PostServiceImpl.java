package com.whisky.blogrestapi.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.whisky.blogrestapi.entity.Post;
import com.whisky.blogrestapi.exception.ResourceNotFoundException;
import com.whisky.blogrestapi.payload.PostDto;
import com.whisky.blogrestapi.payload.PostResponse;
import com.whisky.blogrestapi.repository.PostRepository;
import com.whisky.blogrestapi.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ModelMapper modelMapper;

	private PostDto mapToDto(Post post) {

		PostDto postDto = modelMapper.map(post, PostDto.class);
		return postDto;

	}

	private Post mapToEntity(PostDto postDto) {

		Post post = modelMapper.map(postDto, Post.class);
		return post;

	}

	@Override
	public PostDto createPost(PostDto postDto) {

		// convert DTO to entity
		Post post = mapToEntity(postDto);

		Post newPost = postRepository.save(post);
		// convert entity to DTO
		PostDto responsePost = mapToDto(newPost);
		responsePost.setComments(new ArrayList<>());
		return responsePost;

	}

	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = postRepository.findAll(pageable);

		// get the page content as list
		List<Post> listOfPosts = posts.getContent();
		List<PostDto> content = listOfPosts.stream().map(post -> mapToDto(post))
				.collect(Collectors.toList());
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());

		return postResponse;
	}

	@Override
	public PostDto getPostById(long id) {
		Post loadedPost = postRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		PostDto returnPost = mapToDto(loadedPost);
		return returnPost;
	}

	@Override
	public PostDto updatePostById(PostDto postDto, long id) {
		Post loadedPost = postRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		BeanUtils.copyProperties(postDto, loadedPost, "id");
		Post updatedPost = postRepository.save(loadedPost);
		PostDto returnPost = mapToDto(updatedPost);

		return returnPost;
	}

	@Override
	public String deletePostById(long id) {
		Post loadedPost = postRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		postRepository.delete(loadedPost);

		return "The post with id : " + id + " has been deleted successfully";
	}

}