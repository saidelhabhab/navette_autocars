package com.hormattalah.navette_autocars.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${security.secretKey}")
    private String secretKey;

    private static final int TOKEN_VALIDITY = 3600;

    // Generates a JWT token
    public  String generateToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userName);
    }

    // Creates a JWT token
    public String createToken(Map<String,Object> claims, String userName) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000 * TOKEN_VALIDITY))
                .signWith(getSignKey(),SignatureAlgorithm.HS256).compact();
    }


    // Gets the signing key
    public Key getSignKey(){
        byte[] keyByte= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);

    }


    // Extracts the username from a token
    public  String extractUserName(String token){

        return extractClaim(token,Claims::getSubject);
    }

    // Extracts a claim from a token using a resolver function
    public  <T> T extractClaim(String token, Function<Claims,T> claimResolver){

        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    // Extracts all claims from a token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();

    }

    // Checks if a token is expired
    public  boolean isTokenExpired(String token){

        return extractExpiration(token).before(new Date());
    }


    // Extracts the token's expiration date
    private   Date extractExpiration(String token){

        return extractClaim(token,Claims::getExpiration);
    }


    // Validates a token against user details
    public  boolean validateToken(String token, UserDetails userDetails){
        String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
