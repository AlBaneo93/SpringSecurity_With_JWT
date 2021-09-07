package com.example.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private String name;

  private String email;

  private int loginCount;

  private LocalDateTime lastLoginAt;

  private LocalDateTime createAt;


  public static UserDto of(User user) {
    return UserDto.builder()
                  .name(user.getName())
                  .email(user.getEmail())
                  .lastLoginAt(user.getLastLogIn())
                  .build();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
