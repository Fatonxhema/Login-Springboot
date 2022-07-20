package com.xhemafaton.jwtlogin.config;

import com.xhemafaton.jwtlogin.filter.JwtAuthenticationFilter;
import com.xhemafaton.jwtlogin.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    //Managing authentication process
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
    //Controls the endpoint which are permitted and which aren't
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()//disable CSRF
                .cors()
                .disable()//disable CORS
                .authorizeRequests()
                .antMatchers("/api/login","/api/register").permitAll()//allow only access in this endpoint
                .anyRequest().authenticated()//other request should be authenticated
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// Independent requests and server don't have to manage sessions
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder();}
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
