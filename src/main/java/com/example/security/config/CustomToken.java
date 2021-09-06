package com.example.security.config;

import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomToken extends AbstractAuthenticationToken {

  private final Object principal;

  private Object credentials;


  public CustomToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(true);
  }

  public CustomToken(Object printcipal, Object credentials) {
    super(null);
    this.principal = printcipal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    super.setAuthenticated(authenticated);
  }


  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.credentials = null;
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

}
