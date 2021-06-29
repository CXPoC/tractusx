package com.tsystems.simplepusher.remote;

import com.tsystems.simplepusher.model.ids.IdsResourceMetadata;
import de.fraunhofer.iais.eis.Resource;

import java.net.URI;
import java.util.UUID;

/**
 * Resource repository.
 */
public interface ResourceRepository {

    /**
     * Gets resource
     *
     * @return gets resource
     */
    IdsResourceMetadata getResource(UUID uuid);

    /**
     * Patches resource to concrete connector.
     *
     * @param resource resource
     * @param url      url of consumer connector
     */
    void patchResource(Resource resource, String url, URI connectorID);
}
