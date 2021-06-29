package com.tsystems.simplepusher.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration of resources.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "push", ignoreUnknownFields = false)
public class ProviderConnectorConfig {

    /**
     * Consumers configuration.
     */
    @NonNull
    private List<ConsumerConnectorConfig> consumers;
}
