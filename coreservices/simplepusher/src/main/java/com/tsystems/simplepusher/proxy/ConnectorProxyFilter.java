package com.tsystems.simplepusher.proxy;

import com.tsystems.simplepusher.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Zuul interceptor to catch requester uri of the resource.
 */
@Component
@RequiredArgsConstructor
public class ConnectorProxyFilter implements GatewayFilterFactory<ConnectorProxyFilter.Config> {

    /**
     * Client service.
     */
    private final ClientService clientService;


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //check if request need to be intercepted
            boolean is2xx = Optional.ofNullable(exchange.getResponse().getStatusCode())
                    .map(HttpStatus::is2xxSuccessful).orElse(false);

            if (is2xx) {
                //get resource id and client
                UUID uuid = null;
                String client = null;

                //logic to save relation
                clientService.addClientToResource(uuid, client);

            }

            return chain.filter(exchange);
        };
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    public static class Config {
    }
}
