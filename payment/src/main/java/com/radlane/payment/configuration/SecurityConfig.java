//package com.radlane.payment.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()  // Disable CSRF protection if not needed
//                .authorizeRequests()
//                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Permit Swagger URLs
//                .anyRequest().authenticated()  // Secure other endpoints
//                .and()
//                .httpBasic().disable() // Disable HTTP Basic authentication if not needed
//                .formLogin().disable();  // Disable the default login page
//    }
//}
