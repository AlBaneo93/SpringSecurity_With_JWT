package com.example.security.controller;

import com.example.security.domain.User;
import com.example.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/join")
  public ResponseEntity<?> join(User user) {
    log.info("Join User data :: {}", user.toString());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return ResponseEntity.ok(userService.save(user));
  }

  @GetMapping("/")
  public String index() {
    return "index 페이지";
  }

  @GetMapping("/return")
  public String returnPage(@AuthenticationPrincipal User user) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    if (user != null)
    log.warn("UserData :: {}", user.toString());
    return authentication.toString();
  }

  @GetMapping("/error")
  public String error() {
    return "error 페이지";
  }


}
