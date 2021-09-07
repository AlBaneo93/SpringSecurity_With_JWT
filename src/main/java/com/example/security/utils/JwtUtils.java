package com.example.security.utils;

import com.example.security.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.regex.Pattern;

public class JwtUtils {

  public static final String secret = "mySecret";

  public static final Long tokenExpiers = 30 * 60 * 1000L;

  public static Pattern pattern = Pattern.compile("^Bearer $");

  public String createToken(User user) {
    Claims claims = Jwts.claims().setSubject(String.valueOf(user.getEmail())); // JWT payload에 저장되는 정보단위
    claims.put("role", user.getRole());
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
      return claimsJws.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public String getUserId(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }

  public String parseTokenFromHeader(String header) {
    return pattern.split(header)[1];
  }

}
