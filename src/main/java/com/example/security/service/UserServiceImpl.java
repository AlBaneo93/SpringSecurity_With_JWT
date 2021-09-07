package com.example.security.service;

import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;


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

  public Optional<User> findByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Transactional
  @Override
  public void login(User user) {
    user.setLastLogIn(LocalDateTime.now());
    userRepository.save(user);
  }

  @Override
  public User login(String email, String password) {
    // 패스워드 null 체크
    User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + email));
    // 패스워드 검증
    user.login(passwordEncoder, password);
    user.afterLoginSuccess();
    userRepository.update(user);
    return user;
  }

}
