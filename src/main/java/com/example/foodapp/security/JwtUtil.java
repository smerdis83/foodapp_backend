package com.example.foodapp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // کلید مخفی برای امضای توکن (در production از config بیرونی بگیرید)
    private static final Key SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_MS = 3600_000;  // 1 hour

    // generate a token for a given userId
    public static String generateToken(int userId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION_MS))
                .signWith(SIGNING_KEY)
                .compact();
    }

    // parse and validate token, returns userId or throws if invalid/expired
    public static int validateTokenAndGetUserId(String token) {
        return Integer.parseInt(
                Jwts.parserBuilder()
                        .setSigningKey(SIGNING_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}
