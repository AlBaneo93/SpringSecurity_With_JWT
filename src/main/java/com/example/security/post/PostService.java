package com.example.security.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {

  Page<Post> getAllPost(Pageable pageable);

  Post saveAndUpdate(Post post);

  Post updatePost(Post post);

  void deletePost(Post post);

  Optional<Post> getPost(Long id);

}
