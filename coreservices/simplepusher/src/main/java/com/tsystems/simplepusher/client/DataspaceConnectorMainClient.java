package com.tsystems.simplepusher.client;

import com.tsystems.simplepusher.context.DsFeignConnectorContextConfiguration;
import com.tsystems.simplepusher.model.ids.IdsConnectorDescription;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ConnectorMainClient", url = "${provider.url}",
        configuration = DsFeignConnectorContextConfiguration.class
)
public interface DataspaceConnectorMainClient {

    @GetMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    IdsConnectorDescription getConnector();
}
