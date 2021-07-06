package com.tsystems.simplepusher.client;

import com.tsystems.simplepusher.context.DsFeignConnectorContextConfiguration;
import de.fraunhofer.iais.eis.Connector;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ConnectorMainClient", url = "${provider.url}",
        configuration = DsFeignConnectorContextConfiguration.class
)
public interface DataspaceConnectorMainClient {

    @GetMapping(path = "/")
    Connector getConnectorDescription();
}
