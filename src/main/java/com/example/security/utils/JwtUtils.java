package com.example.security.utils;

import com.example.security.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.regex.Pattern;

@Component
@Slf4j
public class JwtUtils {

  public static final String secret = "mySecret";

  public static final Long tokenExpiers = 30 * 60 * 1000L;

  public static Pattern pattern = Pattern.compile("^Bearer ", Pattern.CASE_INSENSITIVE);

  public String createToken(User user) {
    Claims claims = Jwts.claims().setSubject(String.valueOf(user.getEmail())); // JWT payload에 저장되는 정보단위
    claims.put("role", user.getRole());
    claims.put("name", user.getName());
    Date now = new Date();
    return Jwts.builder()
               .setClaims(claims)
               .setIssuedAt(now)
               .setExpiration(new Date(now.getTime() + tokenExpiers))
               .signWith(SignatureAlgorithm.HS512, secret)
               .compact();
  }

  public boolean isValidToken(String token) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      log.warn("jwt 토큰 유효성 검증 성공");
      return claimsJws.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      log.warn("jwt 토큰 유효성 검증 실패");
      return false;
    }
  }

  public String getUserEmail(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }

  public String getUserName(String token) {
    return String.valueOf(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("name"));
  }

  public String getUserRole(String token) {
    return String.valueOf(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("role"));
  }

  public String parseTokenFromHeader(String header) {
    return header.replaceAll(pattern.pattern(), "");
  }

}
