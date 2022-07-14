package com.xhemafaton.jwtlogin.controller;

import com.xhemafaton.jwtlogin.model.JwtRequest;
import com.xhemafaton.jwtlogin.model.JwtResponse;
import com.xhemafaton.jwtlogin.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.JwtUtil;

@RestController()
@RequestMapping("/api")
public class JwtController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/generateToken")
    public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest jwtRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
                        jwtRequest.getPassword()));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String jwttoken = jwtUtil.generateToken(userDetails);
        JwtResponse response = new JwtResponse(jwttoken);

        return ResponseEntity.ok(response);
    }
}
