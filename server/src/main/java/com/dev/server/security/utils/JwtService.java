package com.dev.server.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dev.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class JwtService {

    @Autowired
    UserRepository userRepository;

    @Value("${secret.jwt}")
    private String secret;

    public String generateToken(String subject){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT
                .create()
                .withSubject(subject)
                .withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC))
                .withExpiresAt(LocalDateTime.now().toInstant(ZoneOffset.UTC).plusSeconds(43200))
                .sign(algorithm);
    }

    public DecodedJWT decoded(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm).build().verify(token);
    }

    private boolean isExpired(String token){
        return decoded(token).getExpiresAt().before(new Date());
    }

    public boolean isValid(String token, UserDetails userDetails){
        String subject = decoded(token).getSubject();
        return !isExpired(token) && (userDetails.getUsername().equals(subject));
    }

    public String getSubject(String token){
        return decoded(token).getSubject();
    }

}
