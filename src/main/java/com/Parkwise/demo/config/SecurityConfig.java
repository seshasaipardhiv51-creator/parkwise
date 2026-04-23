package com.parkwise.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
     .csrf(csrf -> csrf.disable())
     .authorizeHttpRequests(auth -> auth
         .requestMatchers("/location/**", "/auth/**", "/", "/index.html", "/css/**", "/js/**").permitAll()
         .anyRequest().permitAll()
     )
     .oauth2Login(oauth -> oauth
         .defaultSuccessUrl("/index.html", true)
     )
     // ADD THIS BLOCK
     .logout(logout -> logout
         .logoutUrl("/logout") // The endpoint the frontend will call
         .logoutSuccessUrl("/index.html") // Where to go after session is cleared
         .invalidateHttpSession(true)
         .deleteCookies("JSESSIONID")
     )
     .formLogin(form -> form.disable())
     .httpBasic(basic -> basic.disable());

    return http.build();
}
}
