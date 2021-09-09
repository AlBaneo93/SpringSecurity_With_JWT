package com.example.security.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long sequence;

  private String title;

  private String content;

  private String search;

  private LocalDateTime registeredAt;

  private LocalDateTime lastModifiedAt;

  // TODO: 2021-09-09 유저로 변경
  private String author;

  public Post updatePost(Post post) {
    this.title = post.title != null ? post.getTitle() : "";
    this.content = post.content != null ? post.getContent() : "";
    this.lastModifiedAt = LocalDateTime.now();
    return this;
  }

}
