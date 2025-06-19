package com.core.todo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "";
    public String ExtractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String GenerateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 24 hours
                .signWith(getSingingKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = ExtractUsername(token);
        return (username.equals(userDetails.getUsername())) && !IsTokenExpired(token);
    }

    private boolean IsTokenExpired(String token) {
        return ExtractExpiration(token).before(new Date());
        
    }

    private Date ExtractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = ExtractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims ExtractClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSingingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSingingKey() {
        byte [] keybytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keybytes);
    }
}
