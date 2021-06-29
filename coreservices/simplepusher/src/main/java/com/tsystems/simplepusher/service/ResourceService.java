package com.tsystems.simplepusher.service;

public interface ResourceService {

    /**
     * Updates resource.
     *
     * @param resourceId resource id.
     * @param consumerUrl consumer url.
     */
    void updateResource(String resourceId, String consumerUrl);

    /**
     * Updates every single resource.
     */
    void updateEachResource();
}
