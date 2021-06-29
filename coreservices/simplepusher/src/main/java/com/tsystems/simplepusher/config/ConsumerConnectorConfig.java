package com.tsystems.simplepusher.config;

import lombok.*;

import java.util.List;

/**
 * Consumer configuration.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerConnectorConfig {

    /**
     * Consumer url.
     */
    @NonNull
    private String url;

    /**
     * Resources ids to push updates.
     */
    @NonNull
    private List<String> resourcesIds;
}
