package com.cop.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfig {
    @Value("${jwt.secret.key}")
    private String key;
    private Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(auth -> {
                auth.pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .anyExchange().authenticated();
            })
            .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtDecoder(jwtDecoder())))
            .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HMACSHA256");
//        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
        return jwt -> {
            try {
                log.debug("API Gateway: Attempting to decode JWT: {}", jwt);
                return decoder.decode(jwt);
            } catch (Exception e) {
                log.error("API Gateway: JWT decoding failed: {}. Token: {}", e.getMessage(), jwt);
                throw e; // Re-throw to propagate the authentication failure
            }
        };
    }
}
