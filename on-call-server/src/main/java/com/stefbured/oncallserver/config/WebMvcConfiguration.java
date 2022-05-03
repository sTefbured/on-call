package com.stefbured.oncallserver.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Setter
@Configuration
@ConfigurationProperties(prefix = "on-call.web.mvc")
public class WebMvcConfiguration implements WebMvcConfigurer {
    private static final String ALLOWED_ORIGINS_DELIMITER = ";";

    private String allowedOriginsCombined;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOrigins = allowedOriginsCombined.split(ALLOWED_ORIGINS_DELIMITER);
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders(HttpHeaders.LOCATION, HttpHeaders.CONTENT_RANGE);
    }
}
