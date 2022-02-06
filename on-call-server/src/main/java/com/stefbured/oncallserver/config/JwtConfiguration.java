package com.stefbured.oncallserver.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Data
@Configuration
@ConfigurationProperties(prefix = "on-call.jwt")
public class JwtConfiguration {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secretKey);
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
