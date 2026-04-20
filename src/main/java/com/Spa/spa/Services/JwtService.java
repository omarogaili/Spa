package com.Spa.spa.Services;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private String SECRET = "hu5lYn6aQpfw3dsCoaZXfx6yG28B2STNnjOlD1QuiML8xBvBhbVEhLDBFJEQFFbC";
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    public String generateToken (String userName, String role){
        return Jwts.builder().setSubject(userName)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+ 1000* 60 * 60)) // 1h
                    .signWith(key)
                    .compact();
    }

    public Claims validateToken(String token){
        try{
            return Jwts.parserBuilder()
                       .setSigningKey(key)
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
