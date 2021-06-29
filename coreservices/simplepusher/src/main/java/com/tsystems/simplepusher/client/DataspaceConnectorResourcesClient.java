package com.tsystems.simplepusher.client;

import com.tsystems.simplepusher.model.ids.IdsResourceMetadata;
import com.tsystems.simplepusher.model.ids.IdsResourceRepresentation;
import com.tsystems.simplepusher.context.DsFeignConnectorContextConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "ConnectorResourceClient", url = "${provider.url}/admin/api/resources",
        configuration = DsFeignConnectorContextConfiguration.class
)
public interface DataspaceConnectorResourcesClient {
    @PostMapping(path = "/resource", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    String registerResource(@RequestBody IdsResourceMetadata metadata);

    @GetMapping(path = "/{resourceId}")
    IdsResourceMetadata getResource(@PathVariable("resourceId") UUID resourceId);

    @PutMapping(path = "/{resourceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateResource(@PathVariable("resourceId") UUID resourceId, @RequestBody IdsResourceMetadata metadata);

    @DeleteMapping(path = "/{resourceId}")
    void deleteResource(@PathVariable("resourceId") UUID resourceId);

    @PostMapping(path = "/{resourceId}/representation", consumes = MediaType.APPLICATION_JSON_VALUE)
    UUID addRepresentation(@PathVariable("resourceId") UUID resourceId, @RequestBody IdsResourceRepresentation representation);

    @GetMapping(path = "/{resourceId}/{representationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    IdsResourceRepresentation getRepresentation(@PathVariable("resourceId") UUID resourceId, @PathVariable("representationId") UUID representationId);

    @PutMapping(path = "/{resourceId}/{representationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void updateRepresentation(@PathVariable("resourceId") UUID resourceId, @PathVariable("representationId") UUID representationId, @RequestBody IdsResourceRepresentation representation);

    @DeleteMapping(path = "/{resourceId}/{representationId}")
    void deleteRepresentation(@PathVariable("resourceId") UUID resourceId, @PathVariable("representationId") UUID representationId);

    @GetMapping(path = "/{resourceId}/contract")
    String getPolicy(@PathVariable("resourceId") UUID resourceId);

    @PutMapping(path = "/{resourceId}/contract")
    void updatePolicy(@PathVariable("resourceId") UUID resourceId, @RequestBody String policy);
}
