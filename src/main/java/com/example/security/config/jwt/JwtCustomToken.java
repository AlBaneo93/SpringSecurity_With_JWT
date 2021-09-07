package com.example.security.config.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtCustomToken extends AbstractAuthenticationToken {

  public final Object principal;

  public Object credential;

  public JwtCustomToken(Object printcipal, Object credential, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = printcipal;
    this.credential = credential;
    setAuthenticated(true);
  }

  public JwtCustomToken(Object principal, Object credential) {
    super(null);
    setAuthenticated(false);
    this.principal = principal;
    this.credential = credential;
  }

  @Override
  public Object getCredentials() {
    return this.credential;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

}
