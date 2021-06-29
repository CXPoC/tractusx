package com.tsystems.simplepusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@ConfigurationPropertiesScan("com.tsystems.simplepusher.config")
public class SimplePushApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimplePushApplication.class);
    }
}
