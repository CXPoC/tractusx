package com.tsystems.simplepusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.UUID;

/**
 * Application is used to push data to ds connectors.
 */
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableZuulProxy
public class SimplePushApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimplePushApplication.class);
    }
}
