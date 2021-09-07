package com.example.security.config.jwt;

import com.example.security.domain.JwtAuthentication;
import com.example.security.types.AuthConstants;
import com.example.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader(AuthConstants.AUTH_HEADER);

    if (header != null) {
      String token = jwtUtils.parseTokenFromHeader(header);
      if (token != null && jwtUtils.isValidToken(token))
        try {
          // 데이터 파싱
          Long userId = null;
          String userName = jwtUtils.getUserId(token);
          List<GrantedAuthority> authorities = null;

          //파싱 데이터 검증
          if (userId != null && userName != null && authorities.size() > 0) {
            JwtCustomToken authenticationToken = new JwtCustomToken(new JwtAuthentication(userId, userName), null, authorities);
            // setAuthentication(false)이므로 아직 인증되지 않음
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }

        } catch (Exception e) {
          //          e.printStackTrace();
          log.warn("Jwt processing failed: {}", e.getMessage());
        }
    }


    filterChain.doFilter(request, response);
  }

}
