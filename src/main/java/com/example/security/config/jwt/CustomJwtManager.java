package com.example.security.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class CustomJwtManager implements AuthenticationManager {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.warn("CustomJwtManager entered");
    log.warn(authentication.toString());
    return new JwtCustomToken(authentication.getPrincipal(), authentication.getCredentials());
  }

}
