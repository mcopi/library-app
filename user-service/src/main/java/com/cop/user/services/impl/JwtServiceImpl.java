package com.cop.user.services.impl;

import com.cop.user.services.JwtService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.expiration-hour}")
    private long expirationHour;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final JwtEncoder jwtEncoder;

    public JwtServiceImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(expirationHour, ChronoUnit.HOURS);
        Map<String, String> claimsMap = new HashMap<>();

        claimsMap.put("subject", authentication.getName());
        claimsMap.put("issuedAt", Date.from(now).toString());
        claimsMap.put("expiresAt", Date.from(expiresAt).toString());
        claimsMap.put("roleAccess", authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining("|"))
        );

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(Date.from(now).toInstant())
                .subject(authentication.getName())
                .expiresAt(Date.from(expiresAt).toInstant())
                .claim("scope", claimsMap)
                .build();

       JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    @Override
    public String getUsernameFromToken(String token) {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HMACSHA256");

        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}
