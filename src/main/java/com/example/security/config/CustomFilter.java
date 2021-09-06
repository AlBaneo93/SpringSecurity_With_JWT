package com.example.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomFilter extends AbstractAuthenticationProcessingFilter {

  protected CustomFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.warn("CustomFilter Start");
    String username = request.getParameter("email");
    String password = request.getParameter("password");
    log.warn("username: {}, password: {}", username, password);
    log.warn("CustomFilter End");

    return new CustomToken(username, password);
  }

}
