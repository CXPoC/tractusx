package com.tsystems.simplepusher.service.impl;

import com.tsystems.simplepusher.client.DataspaceConnectorMainClient;
import com.tsystems.simplepusher.converter.MetaDataToResouceConverter;
import com.tsystems.simplepusher.remote.ResourceRepository;
import com.tsystems.simplepusher.service.ClientService;
import com.tsystems.simplepusher.service.ResourceService;
import de.fraunhofer.iais.eis.Connector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    /**
     * Resource repository.
     */
    private final ResourceRepository resourceRepository;

    /**
     * Metadata to resource converter.
     */
    private final MetaDataToResouceConverter converter;

    /**
     * Dataspace connector main client.
     */
    private final DataspaceConnectorMainClient mainClient;

    /**
     * Client service.
     */
    private final ClientService clientService;

    @Override
    public void updateResource(UUID resourceId) {
        List<String> uris = clientService.getAllPublicClients(resourceId);

        uris.forEach(consumerUrl ->
                callQuietly(() -> {
                    //get connector description of provider
                    Connector description = mainClient.getConnectorDescription();
                    //get metadata of resource
                    var source = resourceRepository.getResource(resourceId);
                    //convert it to message update
                    var converted = converter.convert(source, description, resourceId);
                    //call patch resource
                    resourceRepository.patchResource(converted, consumerUrl, description.getId());
                })
        );
    }

    /**
     * Suppresses exceptions per message continuing to next update.
     *
     * @param runnable runnable to call
     */
    private void callQuietly(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            log.error("Exception fires in handling update messages.", ex);
        }
    }
}
