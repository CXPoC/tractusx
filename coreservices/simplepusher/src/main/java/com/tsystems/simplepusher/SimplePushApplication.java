package com.tsystems.simplepusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application is used to push data to ds connectors.
 */
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class SimplePushApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimplePushApplication.class);
    }
}
