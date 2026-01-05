package com.dev.server.controllers;

import com.dev.server.DTOs.UserDTO;
import com.dev.server.exceptions.DataExistsException;
import com.dev.server.requests.LoginRequest;
import com.dev.server.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody UserDTO userDTO){
        try {
            return new ResponseEntity<UserDTO>(authService.register(userDTO), HttpStatus.CREATED);
        } catch (DataExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Hubo un error inesperado", HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            return new ResponseEntity<Map<String, Object>>(authService.login(loginRequest), HttpStatus.ACCEPTED);
        } catch (ClassNotFoundException | BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<>("Hubo un error inesperado", HttpStatus.BAD_GATEWAY);
        }
    }

}
