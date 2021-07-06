package com.tsystems.simplepusher.controller;

import com.tsystems.simplepusher.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Resource controllers.
 */
@RestController
@RequiredArgsConstructor
public class ResourceController {

    /**
     * Resource service.
     */
    private final ResourceService resourceService;

    /**
     * Endpoint is used by another service to trigger exact update of resource by id.
     *
     * @param uuid resource id.
     */
    @PostMapping("/resources/trigger")
    public void triggerUpdates(UUID uuid) {
        resourceService.updateResource(uuid);
    }
}
