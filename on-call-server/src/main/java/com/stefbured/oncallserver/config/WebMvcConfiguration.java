package com.stefbured.oncallserver.config;

import com.stefbured.oncallserver.utils.OnCallModelMapper;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public OnCallModelMapper onCallModelMapper() {
        return new OnCallModelMapper();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ModelMapper modelMapper() {
        return onCallModelMapper();
    }
}
