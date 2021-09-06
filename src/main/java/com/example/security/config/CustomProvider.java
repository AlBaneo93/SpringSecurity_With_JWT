package com.example.security.config;

import com.example.security.domain.User;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class CustomProvider implements AuthenticationProvider {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.warn("Custom Provider ---- start");
    log.warn("Principal :: " + String.valueOf(authentication.getPrincipal()));
    //    UserDetails details = userService.loadUserByUsername(String.valueOf(authentication.getPrincipal()));
    CustomToken customToken = (CustomToken) authentication;
    log.warn("Custom Provider::: authentication -- {}", customToken.toString());
    User details = (User) userService.loadUserByUsername(String.valueOf(customToken.getPrincipal()));
    log.warn("User Details ::: {}", details.toString());

    if (!passwordEncoder.matches((String) authentication.getCredentials(), details.getPassword())) {
      log.info("Bad Credentials --- 패스워드 일치하지 않음");
      throw new BadCredentialsException(details.getUsername() + " Invalid Password");
    }
    // 로그인 일자 기록
    LocalDateTime now = LocalDateTime.now();
    details.setLastLogIn(now);
    userService.login(details);
    log.info("User {} logged In at {}", details.getUsername(), now);
    // Details에 유저 정보를 담은 UserDetails 객체를 넣어야, @AuthenticationPrincipal 어노테이션으로 가져올 수 있다.
    // 권한을 포함한 인증 토큰 생성 시, setAuthenticated(true)를 진행함
    CustomToken newCustomToken = new CustomToken(details, details.getPassword(), details.getAuthorities());
    newCustomToken.setDetails(details);

    // NOTE true라고 해주어야 인증이 완료된 것, 인증이 아니면 throw authentication
    //    aa.setAuthenticated(true);  ==> 내부에서 진행해준다
    //    log.warn("is Authenticated??? " + newCustomToken.isAuthenticated());
    //    log.warn("Authorities ::: " + newCustomToken.getAuthorities());
    //    log.warn("Custom Provider ---- end, data: {}", newCustomToken.toString());
    return newCustomToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return CustomToken.class.isAssignableFrom(authentication);
  }

}
