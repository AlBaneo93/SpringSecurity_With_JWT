package com.example.security.config;

import com.example.security.domain.User;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

  private final UserService service;

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    try {
      if (authentication != null && authentication.getDetails() != null) {
        request.getSession().invalidate();
        //  쿠키 삭제
        Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("JSESSIONID")).forEach(cookie -> cookie.setValue(null));
        if (authentication.isAuthenticated()) {
          authentication.setAuthenticated(false);
          // NOTE 로그아웃을 위한 DB 업데이트?
          service.logout((User) authentication.getPrincipal());
          log.info("User {} logged Out at {}", authentication.getName(), LocalDateTime.now());
        }
      }

      response.setStatus(HttpServletResponse.SC_OK);
      response.sendRedirect("/");
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error Occurred when user logout");
    }
  }

}
