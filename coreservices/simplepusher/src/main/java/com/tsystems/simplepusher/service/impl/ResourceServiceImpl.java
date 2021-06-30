package com.tsystems.simplepusher.service.impl;

import com.tsystems.simplepusher.client.DataspaceConnectorMainClient;
import com.tsystems.simplepusher.config.ProviderConnectorConfig;
import com.tsystems.simplepusher.converter.MetaDataToResouceConverter;
import com.tsystems.simplepusher.remote.ResourceRepository;
import com.tsystems.simplepusher.service.ResourceService;
import de.fraunhofer.iais.eis.Connector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    /**
     * Provider config.
     */
    private final ProviderConnectorConfig providerConfig;

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

    @Override
    public void updateResource(UUID resourceId, String consumerUrl) {
        //get connector description of provider
        Connector description = mainClient.getConnectorDescription();
        //get metadata of resource
        var source = resourceRepository.getResource(resourceId);
        //convert it to message update
        var converted = converter.convert(source, description, resourceId);
        //call patch resource
        resourceRepository.patchResource(converted, consumerUrl, description.getId());
    }

    @Override
    //ToDO: better way to trigger resource update
    @Scheduled(fixedDelay = 30000)
    public void updateEachResource() {
        providerConfig.getConsumers().forEach(consumer ->
                consumer.getResourcesIds().forEach(resourceId -> callQuietly(() -> this.updateResource(UUID.fromString(resourceId) , consumer.getUrl()))));
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
