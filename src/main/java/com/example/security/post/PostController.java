package com.example.security.post;

import com.example.security.exception.PostNotFoundException;
import com.example.security.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidParameterException;

import static com.example.security.utils.ApiUtils.success;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @GetMapping("/posts")
  public ApiUtils.ApiResults<Page<Post>> getAllPosts(@RequestBody Post post, @PageableDefault Pageable pageable) {
    return success(postService.getAllPost(pageable));
  }

  @GetMapping("/post/{seq}")
  public ApiUtils.ApiResults<Post> getPostById(@PathVariable("seq") Long seq) throws PostNotFoundException {
    return success(postService.getPost(seq).orElseThrow(PostNotFoundException::new));
  }

  @PostMapping("/post")
  public ApiUtils.ApiResults<?> addPost(@RequestBody Post post) {
    try {
      postService.saveAndUpdate(post);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new InvalidParameterException();
    }
  }

  @DeleteMapping("/post")
  public ApiUtils.ApiResults<?> deletePost(@RequestBody Post post) throws IOException {
    try {
      postService.deletePost(post);
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new IOException();
    }

  }

  @PutMapping("/post")
  public ApiUtils.ApiResults<?> updatePost(@RequestBody Post post) throws IOException {
    try {
      Post dbPost = postService.getPost(post.getSequence()).orElseThrow(PostNotFoundException::new);
      postService.updatePost(dbPost.updatePost(post));
      return success(Boolean.TRUE);
    } catch (Exception e) {
      throw new IOException();
    }
  }

}
