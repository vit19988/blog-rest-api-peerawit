package com.whisky.blogrestapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whisky.blogrestapi.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findByPostId(long postId);


}
