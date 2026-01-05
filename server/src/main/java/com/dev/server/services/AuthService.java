package com.dev.server.services;

import com.dev.server.DTOs.UserDTO;
import com.dev.server.entities.UserEntity;
import com.dev.server.exceptions.DataExistsException;
import com.dev.server.repositories.UserRepository;
import com.dev.server.requests.LoginRequest;
import com.dev.server.security.services.UserDetailServiceImp;
import com.dev.server.security.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailServiceImp userDetailsService;

    public UserDTO register(UserDTO userDTO) throws DataExistsException{
        if( userRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new DataExistsException("User already exists");
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setCompleteName(userDTO.getCompleteName());
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setId(userRepository.save(userEntity).getId().toString());
        return userDTO;
    }

    public Map<String, Object> login(LoginRequest loginRequest) throws ClassNotFoundException{
        Optional<UserEntity> userOp = userRepository.findByEmail(loginRequest.email());
        if(userOp.isEmpty()){
            throw new ClassNotFoundException("Invalid email");
        }
        Map<String, Object> response = new HashMap<>();
        Authentication authentication = authenticate(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(loginRequest.email());
        response.put("token", token);
        userOp = Optional.empty();
        return response;
    }

    private Authentication authenticate(LoginRequest loginRequest){
        String email = loginRequest.email();
        String password = loginRequest.password();

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if(userDetails == null) throw new BadCredentialsException("Invalid email or password");

        if(!passwordEncoder.matches(password, userDetails.getPassword())) throw new BadCredentialsException("Invalid password");

        return new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
    }

}
