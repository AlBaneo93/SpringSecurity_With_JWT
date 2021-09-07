package com.example.security.config.jwt;

import com.example.security.types.AuthConstants;
import com.example.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomJwtFilter extends AbstractAuthenticationProcessingFilter {


  private final JwtUtils jwtUtils;

  public CustomJwtFilter(String defaultFilterProcessesUrl, JwtUtils jwtUtils) {
    super(defaultFilterProcessesUrl);
    this.jwtUtils = jwtUtils;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String header = request.getHeader(AuthConstants.AUTH_HEADER);
    String userName = null;
    String token = null;
    if (header != null) {
      token = jwtUtils.parseTokenFromHeader(header);
      if (token != null && jwtUtils.isValidToken(token))
        userName = jwtUtils.getUserId(token);
    }
    return new JwtCustomToken(userName, token);
  }

}
