package io.jpass.auth.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jpass.security.jwt")
public record JwtProperties(
        String secret,
        long expiration
) {
}
