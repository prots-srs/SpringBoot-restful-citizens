package com.protsdev.citizens;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class HttpBasicSecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        User.UserBuilder users = User.builder();

        UserDetails user = users
                .username("user")
                .password(passwordEncoder.encode("_user_"))
                .roles("USER")
                .build();

        UserDetails admin = users
                .username("admin")
                .password(passwordEncoder.encode("_admin_"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(requests -> {
                    requests
                            .dispatcherTypeMatchers(DispatcherType.FORWARD,
                                    DispatcherType.ERROR)
                            .permitAll()
                            .requestMatchers(
                                    "/api/**",
                                    "/error",
                                    "/actuator/health")
                            .permitAll()
                            .requestMatchers("/actuator/**")
                            .hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .csrf(Customizer.withDefaults())
                .securityMatcher("/h2-console/**", "/actuator/**");

        return http.build();
    }
}
