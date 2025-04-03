package com.librarymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                // Permit all for development
                .antMatchers("/**").permitAll()
                // The lines below are commented out for development 
                // but should be enabled for production
                /*
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/users/register").permitAll()
                .antMatchers("/api/librarians/login").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/librarians/**").hasRole("LIBRARIAN")
                .anyRequest().authenticated()
                */
            .and()
            .httpBasic()
            .and()
            .headers().frameOptions().disable(); // For H2 console
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
