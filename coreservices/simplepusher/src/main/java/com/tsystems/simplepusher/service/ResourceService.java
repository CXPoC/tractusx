package com.tsystems.simplepusher.service;

import java.util.UUID;

public interface ResourceService {

    /**
     * Updates resource.
     *
     * @param resourceId resource id.
     * @param consumerUrl consumer url.
     */
    void updateResource(UUID resourceId, String consumerUrl);

    /**
     * Updates every single resource.
     */
    void updateEachResource();
}
