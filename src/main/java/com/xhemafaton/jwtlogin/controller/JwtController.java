package com.xhemafaton.jwtlogin.controller;

import com.xhemafaton.jwtlogin.model.JwtRequest;
import com.xhemafaton.jwtlogin.model.JwtResponse;
import com.xhemafaton.jwtlogin.model.UserModel;
import com.xhemafaton.jwtlogin.service.CustomUserDetailsService;
import com.xhemafaton.jwtlogin.util.JwtUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;

@RestController
@RequestMapping("/api")
public class JwtController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody UserModel userModel){
            UserModel userModel1 = customUserDetailsService.register(userModel);
            return ResponseEntity.ok(userModel1);
    }
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> generateToken(@RequestBody @NotNull JwtRequest jwtRequest){
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword());

        authenticationManager.authenticate(credentials);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String jwttoken = jwtUtil.generateToken(userDetails);

        JwtResponse response = new JwtResponse(jwttoken);
        ResponseEntity responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping("/currentUser")
    public UserModel getCurrentUser(@NotNull Principal principal){
        UserDetails userdetails = customUserDetailsService.loadUserByUsername(principal.getName());
        return (UserModel) userdetails;
    }
}
