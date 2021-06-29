package com.tsystems.simplepusher.model.ids;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.List;

@Data
@NoArgsConstructor
public class IdsConnectorDescription {
    @JsonProperty("@type")
    public String type;
    @JsonProperty("@id")
    public URI id;
    @JsonProperty("ids:publicKey")
    public IdsPublicKey idsPublicKey;
    @JsonProperty("ids:description")
    public List<IdsDescription> idsDescription;
    @JsonProperty("ids:version")
    public String idsVersion;
    @JsonProperty("ids:hasDefaultEndpoint")
    public IdsHasDefaultEndpoint idsHasDefaultEndpoint;
    @JsonProperty("ids:outboundModelVersion")
    public String idsOutboundModelVersion;
    @JsonProperty("ids:inboundModelVersion")
    public List<String> idsInboundModelVersion;
    @JsonProperty("ids:title")
    public List<IdsTitle> idsTitle;
    @JsonProperty("ids:securityProfile")
    public IdsSecurityProfile idsSecurityProfile;
    @JsonProperty("ids:curator")
    public IdsCurator idsCurator;
    @JsonProperty("ids:maintainer")
    public IdsMaintainer idsMaintainer;
}
