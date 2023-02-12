package com.xhemafaton.jwtlogin.config;

import com.xhemafaton.jwtlogin.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    //Controls the endpoint which are permitted and which aren't
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()//disable CSRF
                .cors()
                .disable()//disable CORS
                .authorizeHttpRequests()
                .requestMatchers("/api/login", "/api/register").permitAll()//allow only access in this endpoint
                .anyRequest().authenticated()//other request should be authenticated
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// Independent requests and server don't have to manage sessions
        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
