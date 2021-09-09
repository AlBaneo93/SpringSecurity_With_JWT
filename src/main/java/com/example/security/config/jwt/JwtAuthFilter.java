package com.example.security.config.jwt;

import com.example.security.domain.User;
import com.example.security.service.UserService;
import com.example.security.types.AuthConstants;
import com.example.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;

  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader(AuthConstants.AUTH_HEADER);
    if (header != null) {
      String token = jwtUtils.parseTokenFromHeader(header);
      if (token != null && jwtUtils.isValidToken(token))
        try {
          // 데이터 파싱
          String email = jwtUtils.getUserEmail(token);
          String role = jwtUtils.getUserRole(token);
          //파싱 데이터 검증
          // email(아이디)가 null이 아니고, 권한도 null이 아니어야 인증 성공
          if (email != null && role != null) {
            // 토큰에서 가져온 정보 (유저 아이디, 권한)를 가지고 DB조회
            User user = (User) userService.loadUserByUsername(email);
            // JwtCustomToken의 생성자 중 권한까지 인자로 들어있는 생성자는 토큰을 만들면서 인증 성공 여부 확인
            JwtCustomToken authenticationToken = new JwtCustomToken(user, null, Collections.singleton(new SimpleGrantedAuthority(role)));
            // 인증 된 AuthenticationToken을 SecurityContext에 넣고 다음 필터로
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        } catch (Exception e) {
          log.warn("Jwt processing failed: {}", e.getMessage());
        }
    }


    filterChain.doFilter(request, response);
  }

}
