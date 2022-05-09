package com.stefbured.oncallserver.config;

import lombok.Setter;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Setter
@Configuration
@EnableScheduling
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
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.LOCATION, HttpHeaders.CONTENT_RANGE);
    }

    @Bean
    public TomcatContextCustomizer sameSiteCookiesConfig() {
        return context -> {
            final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
            cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
            context.setCookieProcessor(cookieProcessor);
        };
    }
}
