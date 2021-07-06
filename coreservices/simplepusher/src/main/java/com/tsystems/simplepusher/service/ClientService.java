package com.tsystems.simplepusher.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Service is used to save client's URIs.
 */
public interface ClientService {

    /**
     * Get public client's URIs by resource id.
     *
     * @param uuid resource id
     * @return client's uris
     */
    List<String> getAllPublicClients(UUID uuid);

    /**
     * Save client uri to concrete resource.
     *
     * @param uuid resource id
     * @param uri client uri
     */
    void addClientToResource(UUID uuid, String uri);
}
