package com.example.security.config.jwt;

import com.example.security.domain.JwtAuthentication;
import com.example.security.domain.User;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
@RequiredArgsConstructor
public class CustomJwtProvider implements AuthenticationProvider {

  private final UserService userService;

  @SneakyThrows
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.info("CustomJwtProvider --- {}", authentication.toString());
    JwtCustomToken token = (JwtCustomToken) authentication;
    if (token.getPrincipal() == null) {
      throw new BadCredentialsException("로그인 불가");
    }
    return processUserAuthentication((String) token.getPrincipal(), (String) token.getCredentials());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(JwtCustomToken.class);
  }

  private Authentication processUserAuthentication(String email, String password) throws Exception {
    try {
      User user = userService.login(email, password);
      JwtCustomToken jwtCustomToken = new JwtCustomToken(new JwtAuthentication(user.getId(), user.getName()), null, user.getAuthorities());
      jwtCustomToken.setDetails(user);
      return jwtCustomToken;
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
  }

}
