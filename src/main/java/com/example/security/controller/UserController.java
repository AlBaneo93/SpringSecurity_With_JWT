package com.example.security.controller;

import com.example.security.config.jwt.JwtCustomToken;
import com.example.security.domain.LoginRequest;
import com.example.security.domain.LoginResult;
import com.example.security.domain.User;
import com.example.security.domain.UserDto;
import com.example.security.service.UserService;
import com.example.security.utils.ApiUtils;
import com.example.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.example.security.utils.ApiUtils.success;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

  private final AuthenticationManager authenticationManager;

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtils jwtUtils;

  @PostMapping("/join")
  public ResponseEntity<?> join(User user) {
    log.info("Join User data :: {}", user.toString());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return ResponseEntity.ok(userService.save(user));
  }

  @GetMapping("/")
  public String index() {
    return "index 페이지";
  }

  @GetMapping("/return")
  public String returnPage(@AuthenticationPrincipal User user) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    if (user != null)
    log.warn("UserData :: {}", user.toString());
    return authentication.toString();
  }

  @GetMapping("/error")
  public String error() {
    return "error 페이지";
  }


  @PostMapping("/autenticate")
  public ApiUtils.ApiResults<UserDetails> authentication(@RequestBody User user) {
    return success(userService.loadUserByUsername(user.getEmail()));
  }

  @PostMapping("/login")
  public ApiUtils.ApiResults<?> userLogin(@RequestBody LoginRequest request) throws Exception {
    try {
      Authentication authentication = authenticationManager.authenticate(new JwtCustomToken(request.getPrincipal(), request.getCredentials()));
      User user = (User) authentication.getPrincipal();
      // jwt 발급
      String token = jwtUtils.createToken(user);
      return success(new LoginResult(token, UserDto.of(user)));
    } catch (AuthenticationException e) {
      throw new Exception();
    }
  }

}
