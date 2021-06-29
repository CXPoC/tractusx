package com.tsystems.simplepusher.remote.impl;

import com.tsystems.simplepusher.client.DataspaceConnectorResourcesClient;
import com.tsystems.simplepusher.model.ids.IdsResourceMetadata;
import com.tsystems.simplepusher.config.DapsConfig;
import com.tsystems.simplepusher.remote.ResourceRepository;
import de.fraunhofer.iais.eis.DynamicAttributeToken;
import de.fraunhofer.iais.eis.DynamicAttributeTokenBuilder;
import de.fraunhofer.iais.eis.Resource;
import de.fraunhofer.iais.eis.TokenFormat;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import de.fraunhofer.isst.ids.framework.communication.broker.BrokerIDSMessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;

/**
 * Resource repository implementation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ResourceRepositoryImpl implements ResourceRepository {

    /**
     * Resources client.
     */
    private final DataspaceConnectorResourcesClient resourcesClient;

    /**
     * Serializer.
     */
    private final Serializer serializer;

    /**
     * Ok http client.
     */
    private final OkHttpClient okHttpClient;

    /**
     * Daps configuration.
     */
    private final DapsConfig dapsConfig;


    @Override
    public IdsResourceMetadata getResource(UUID uuid) {
        return resourcesClient.getResource(uuid);
    }

    @Override
    public void patchResource(Resource resource, String url, URI connectorID) {
        updateResourceRemote(url, resource, connectorID);
    }

    @SneakyThrows
    public Response updateResourceRemote(String url, Resource resource, URI connectorID) {
        var header = BrokerIDSMessageUtils.buildResourceUpdateMessage(getDAT(), "4.0.0",
                connectorID, resource);
        var payload = serializer.serialize(resource);
        var body = BrokerIDSMessageUtils.buildRequestBody(header, payload);

        return sendMessage(url, body);
    }

    /**
     * Creating dynamic attribute token.
     *
     * @return created {@link DynamicAttributeToken}
     */
    private DynamicAttributeToken getDAT() {
        return new DynamicAttributeTokenBuilder()
                ._tokenFormat_(TokenFormat.JWT)
                ._tokenValue_(dapsConfig.getToken())
                .build();
    }

    @SneakyThrows
    private Response sendMessage(String brokerURI, RequestBody requestBody) {
        return okHttpClient.newCall((new Request.Builder()).url(brokerURI).post(requestBody).build()).execute();
    }
}
