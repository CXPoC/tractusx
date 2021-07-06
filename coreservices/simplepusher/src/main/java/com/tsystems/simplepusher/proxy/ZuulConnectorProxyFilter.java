package com.tsystems.simplepusher.proxy;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tsystems.simplepusher.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.tsystems.simplepusher.model.enums.RouteNames.PROVIDER_CONNECTOR;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

/**
 * Zuul interceptor to catch requester uri of the resource.
 */
@Component
@RequiredArgsConstructor
public class ZuulConnectorProxyFilter extends ZuulFilter {

    /**
     * Client service.
     */
    private final ClientService clientService;

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (ctx.get(PROXY_KEY) != null) && ctx.get(PROXY_KEY).equals(PROVIDER_CONNECTOR);
    }

    @Override
    public Object run() {

        //check if request need to be intercepted
        if (HttpStatus.valueOf(RequestContext.getCurrentContext().getResponse().getStatus()).is2xxSuccessful()) {

        }

        //get resource id and client
        UUID uuid = null;
        String client = RequestContext.getCurrentContext().getRequest().getRemoteHost();

        //logic to save relation
        clientService.addClientToResource(uuid, client);

        return null;
    }
}
