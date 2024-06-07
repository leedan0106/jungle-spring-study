//package com.jungle.board.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .antMatchers("/public/**").permitAll() // /public/** 경로는 인증 없이 접근 가능
//                                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
//                )
//                .httpBasic(); // HTTP Basic 인증 사용
//
//        return http.build();
//    }
//}
