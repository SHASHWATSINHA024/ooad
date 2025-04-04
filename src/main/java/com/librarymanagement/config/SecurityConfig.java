package com.librarymanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disable CSRF for testing or non-browser-based clients.
            .authorizeRequests()
                // Allow unauthenticated access to certain paths
                .antMatchers("/users/**", "/books/**").permitAll() // Allow all book-related and user-related paths
                .anyRequest().permitAll()  // Allow all other requests to be unauthenticated
            .and()
            .formLogin().disable()  // Disable form login
            .httpBasic().disable();  // Disable basic HTTP authentication
    }
}
