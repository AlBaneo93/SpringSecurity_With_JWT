package com.example.security.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  @Override
  public Page<Post> getAllPost(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

  @Override
  public Post saveAndUpdate(Post post) {
    return postRepository.save(post);
  }

  @Override
  @Transactional
  public Post updatePost(Post post) {
    return postRepository.save(post);
  }

  @Override
  @Transactional
  public void deletePost(Post post) {
    postRepository.delete(post);
  }

  @Override
  public Optional<Post> getPost(Long id) {
    return postRepository.findById(id);
  }

}
