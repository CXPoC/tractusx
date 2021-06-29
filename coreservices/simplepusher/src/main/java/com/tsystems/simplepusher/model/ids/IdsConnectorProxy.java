package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class IdsConnectorProxy {
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public String id;
    @JsonProperty("ids:proxyAuthentication")
    public IdsProxyAuthentication idsProxyAuthentication;
    @JsonProperty("ids:proxyURI")
    public IdsProxyURI idsProxyURI;
    @JsonProperty("ids:noProxy")
    public List<IdsNoProxy> idsNoProxy;
}
