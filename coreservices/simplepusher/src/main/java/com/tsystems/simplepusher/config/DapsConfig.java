package com.tsystems.simplepusher.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DapsConfig {

    @Value("${daps.token}")
    private String token;
}
