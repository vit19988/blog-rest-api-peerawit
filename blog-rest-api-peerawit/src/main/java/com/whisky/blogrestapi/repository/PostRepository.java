package com.whisky.blogrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.whisky.blogrestapi.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

}
