package com.example.security.service;

import com.example.security.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  User save(User user);

  void logout(User user);

  void login(User user);

}
