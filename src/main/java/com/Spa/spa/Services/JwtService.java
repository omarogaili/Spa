package com.Spa.spa.Services;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${SECRET_TOKEN}")
    private String secretey;


    private final Key getKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretey));
    }

    public String generateToken (String userName, String role){
        return Jwts.builder().setSubject(userName)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+ 1000* 60 * 60)) // 1h
                    .signWith(getKey())
                    .compact();
    }

    public Claims validateToken(String token){
        try{
            return Jwts.parserBuilder()
                       .setSigningKey(getKey())
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch(Exception e){
            return null;
        }
    }

    public String readRoleClaim(String token){
        Claims claims = validateToken(token);
        if (claims == null) {
            return null;
        }
        return claims.get("role", String.class);
    }

    public String readSubject(String token){
        Claims claims = validateToken(token);
        return claims != null ? claims.getSubject(): null;
    }
}
