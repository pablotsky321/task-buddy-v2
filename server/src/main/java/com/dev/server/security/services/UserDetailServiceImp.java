package com.dev.server.security.services;

import com.dev.server.entities.UserEntity;
import com.dev.server.repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserEntity> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()){
            throw new UsernameNotFoundException("user with this email does not exists");
        }

        return findUser.get();

    }
}
