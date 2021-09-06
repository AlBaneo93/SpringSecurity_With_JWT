package com.example.security.config;

import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;

  private final CustomLogoutSuccessHandler logoutSuccessHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .cors().disable()
        .httpBasic().disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/api/authenticate", "/api/join").permitAll()
        .antMatchers("/api/error", "/api").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .usernameParameter("email")
        .loginPage("/login")
        .and()
        .logout()
        .logoutUrl("/api/logout")
        .logoutSuccessHandler(logoutSuccessHandler)
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .and()
        .addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public CustomFilter customFilter() {
    CustomFilter filter = new CustomFilter("/api/authenticate");
    filter.setAuthenticationManager(customManager());
    filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/api/error"));
    filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/api/"));
    filter.afterPropertiesSet();
    return filter;
  }

  @Bean
  public CustomManager customManager() {
    return new CustomManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CustomProvider customProvider() {
    return new CustomProvider(userService, passwordEncoder());
  }

}
