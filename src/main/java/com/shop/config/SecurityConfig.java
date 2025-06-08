package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Autowired
  MemberService memberService;
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
            .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
            .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest()
            .authenticated()
        ).formLogin(formLoginCustomizer -> formLoginCustomizer
            .loginPage("/members/login")
            .defaultSuccessUrl("/", true)
            .usernameParameter("email")
            .failureUrl("/members/login/error"))
        .exceptionHandling(e -> e
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        );
    return http.build();
  }
}

