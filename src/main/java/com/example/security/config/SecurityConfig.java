package com.example.security.config;

import com.example.security.config.jwt.CustomJwtProvider;
import com.example.security.config.jwt.JwtAuthFilter;
import com.example.security.config.jwt.JwtAuthenticationEntryPoint;
import com.example.security.service.UserService;
import com.example.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtils jwtUtils;

  private final CustomLogoutSuccessHandler logoutSuccessHandler;
  //  form login 설정
  //  @Override
  //  protected void configure(HttpSecurity http) throws Exception {
  //    http.csrf().disable()
  //        .cors().disable()
  //        .httpBasic().disable()
  //        .authorizeRequests()
  //        .antMatchers(HttpMethod.POST, "/api/authenticate", "/api/join").permitAll()
  //        .antMatchers("/api/error", "/api").permitAll()
  //        .anyRequest().authenticated()
  //        .and()
  //        .formLogin()
  //        .usernameParameter("email")
  //        .loginPage("/login")
  //        .and()
  //        .logout()
  //        .logoutUrl("/api/logout")
  //        .logoutSuccessHandler(logoutSuccessHandler)
  //        .clearAuthentication(true)
  //        .invalidateHttpSession(true)
  //        .deleteCookies("JSESSIONID")
  //        .and()
  //        .addFilterBefore(customJwtFilter(), UsernamePasswordAuthenticationFilter.class);
  //  }

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  // jwt 설정
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().formLogin().disable().httpBasic().disable().cors().disable()
        .authorizeRequests()
        .antMatchers("/api/login", "/api/join").permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  //  @Bean
  public CustomFilter customFilter() {
    CustomFilter filter = new CustomFilter("/api/authenticate");
    filter.setAuthenticationManager(customManager());
    filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/api/error"));
    filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/api/"));
    filter.afterPropertiesSet();
    return filter;
  }

  //  @Bean
  public CustomManager customManager() {
    return new CustomManager();
  }


  //  @Bean
  public CustomProvider customProvider() {
    return new CustomProvider(userService, passwordEncoder);
  }

  //  @Bean
  //  public CustomJwtFilter customJwtFilter() {
  //    CustomJwtFilter filter = new CustomJwtFilter("/api/authenticate", new JwtUtils());
  //    filter.setAuthenticationManager(customManager());
  //    filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/api/error"));
  //    filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/api/"));
  //    filter.afterPropertiesSet();
  //    return filter;
  //  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public CustomJwtProvider customJwtProvider() {
    return new CustomJwtProvider(userService);
  }

  @Bean
  public JwtAuthFilter jwtAuthFilter() {
    return new JwtAuthFilter(jwtUtils, userService);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(customJwtProvider());
  }

  //  @Bean
  //  @Override
  //  public AuthenticationManager authenticationManager() throws Exception {
  //    return super.authenticationManagerBean();
  //  }

}
