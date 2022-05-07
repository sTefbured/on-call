package com.stefbured.oncallserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class OnCallServerApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC/Greenwich"));
        SpringApplication.run(OnCallServerApplication.class, args);
    }
}
