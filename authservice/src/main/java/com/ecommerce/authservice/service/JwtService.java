package com.ecommerce.authservice.service;


import com.ecommerce.authservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSignKey(){

        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public  String generateToken(User user){

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role",user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey())
                .compact();


    }

    public Claims extractAllClaims(String token){

        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){

        Claims claims= extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public  boolean isTokenIsExpired(String token){
        return extractExpiration(token).before(new Date());
    }


    public boolean isTokenValid (String token , UserDetails userDetails){
        String userName= extractUsername(token);
        return userName.equals(userDetails.getUsername()) && !isTokenIsExpired(token);
    }


}
