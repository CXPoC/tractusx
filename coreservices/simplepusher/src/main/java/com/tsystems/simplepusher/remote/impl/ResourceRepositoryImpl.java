package com.tsystems.simplepusher.remote.impl;

import com.tsystems.simplepusher.client.DataspaceConnectorResourcesClient;
import com.tsystems.simplepusher.model.ids.IdsResourceMetadata;
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
     * Info model version.
     */
    private final static String INFO_MODEL_VERSION = "4.0.0";
    /**
     * Url format.
     */
    private final static String MESSAGE_REQUEST_FORMAT = "%s/api/ids/data";
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

    @Override
    public IdsResourceMetadata getResource(UUID uuid) {
        return resourcesClient.getResource(uuid);
    }

    @Override
    public void patchResource(Resource resource, String url, URI connectorID) {
        updateResourceRemote(url, resource, connectorID).body().close();
    }

    @SneakyThrows
    public Response updateResourceRemote(String url, Resource resource, URI connectorID) {
        var header = BrokerIDSMessageUtils.buildResourceUpdateMessage(getDAT(), INFO_MODEL_VERSION,
                connectorID, resource);
        var payload = serializer.serialize(resource);
        var body = BrokerIDSMessageUtils.buildRequestBody(header, payload);

        return sendMessage(String.format(MESSAGE_REQUEST_FORMAT, url), body);
    }

    /**
     * Creating dynamic attribute token.
     *
     * @return created {@link DynamicAttributeToken}
     */
    private DynamicAttributeToken getDAT() {
        return new DynamicAttributeTokenBuilder()
                ._tokenFormat_(TokenFormat.JWT)
                //ToDo: daps token handling
//                ._tokenValue_(dapsConfig.getToken())
                .build();
    }

    @SneakyThrows
    private Response sendMessage(String url, RequestBody requestBody) {
        return okHttpClient.newCall((new Request.Builder()).url(url).post(requestBody).build()).execute();
    }
}
