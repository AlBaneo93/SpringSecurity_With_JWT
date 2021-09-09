package com.example.security.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class JwtAuthentication {

  public final Long id;

  public final String name;

  public JwtAuthentication(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
