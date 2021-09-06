package com.example.security.service;

import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(username).orElseThrow();
    log.info("User in Service :: {}", user.toString());
    return user;
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Transactional
  @Override
  public void logout(User user) {
    user.setLastLogOut(LocalDateTime.now());
    userRepository.save(user);
  }

  @Transactional
  @Override
  public void login(User user) {
    user.setLastLogIn(LocalDateTime.now());
    userRepository.save(user);
  }


}
