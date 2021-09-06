package com.example.security.config;

import com.example.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class CustomManager implements AuthenticationManager {

  @Autowired
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.warn("Custom Manager --- Start");
    log.warn("{}", authentication.toString());
    String username = (String) authentication.getPrincipal();
    UserDetails userDetails = userService.loadUserByUsername(username);

    // isUserExists??
    // isPasswordCorrect?
    // isUserNotExpired?

    log.warn("Custom Manager --- End");
    return new CustomToken(userDetails.getUsername(), userDetails.getPassword());
  }

}
